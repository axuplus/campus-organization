package com.smoke.service.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smoke.service.config.OneNetUrlConfig;
import com.smoke.service.mapper.SmokeDataMapper;
import com.smoke.service.mapper.SmokeDeviceMapper;
import com.smoke.service.model.dto.DeviceDto;
import com.smoke.service.model.dto.DeviceExcelDto;
import com.smoke.service.model.entity.SmokeData;
import com.smoke.service.model.entity.SmokeDevice;
import com.smoke.service.model.vo.DeviceListVo;
import com.smoke.service.model.vo.DeviceVo;
import com.smoke.service.service.SmokeDeviceService;
import com.smoke.service.utils.*;
import com.smoke.service.utils.Enum.ErrorCodeEnum;
import com.smoke.service.utils.wrapper.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-11-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmokeDeviceServiceImpl extends ServiceImpl<SmokeDeviceMapper, SmokeDevice> implements SmokeDeviceService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static Map header = new HashMap();

    static {
        header.put("api-key", "EEaWTfScdgkPYE1MxLfN=BV=a60=");
    }

    @Autowired
    private SmokeDeviceMapper deviceMapper;

    @Autowired
    private SmokeDataMapper dataMapper;

    @Override
    public Wrapper addDevice(DeviceDto deviceDto) {
        if (PublicUtil.isNotEmpty(deviceDto)) {
            SmokeDevice device = new ModelMapper().map(deviceDto, SmokeDevice.class);
            device.setId(GlobalUtils.generateId());
            device.setState(0);
            device.setCreatedTime(LocalTimeUtils.LocalDateTimeToString());
            if (PublicUtil.isNotEmpty(deviceMapper.selectOne(new QueryWrapper<SmokeDevice>().eq("device_id", deviceDto.getDeviceId())))) {
                return WrapMapper.error("设备不可重复添加");
            }
            HashMap<String, Object> map = new HashMap<>();
            List<String> s = new ArrayList<>();
            s.add("china");
            s.add("mobile");
            map.put("title", deviceDto.getDeviceName());
            map.put("tags", s);
            map.put("protocol", "LWM2M");
            map.put("private", "true");
            map.put("obsv", "true");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(deviceDto.getDeviceId(), device.getId().toString());
            map.put("auth_info", jsonObject);
            String doPost = HttpUtils.DO_POST(OneNetUrlConfig.ADD_DEVICE, new Gson().toJson(map), header, null);
            String code = new JsonParser().parse(doPost).getAsJsonObject().get("error").getAsString();
            logger.warn("OneNet添加设备请求 -------------------------> {}", code);
            if (!"succ".equals(code)) {
                throw new BizException(ErrorCodeEnum.PUB10000009);
            }
            String returnId = new JsonParser().parse(doPost).getAsJsonObject().get("data").getAsJsonObject().get("device_id").getAsString();
            device.setReturnId(returnId);
            deviceMapper.insert(device);
            return WrapMapper.ok("添加成功");
        }
        return WrapMapper.error("信息不能为空");
    }

    @Override
    public Wrapper batchAddDevices(MultipartFile file) {
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000006);
        }
        String fileName = file.getOriginalFilename();
        if (!".xlsx".equals(PathUtils.getExtension(fileName))) {
            logger.info("文件名格式不正确,请使用后缀名为.XLSX的文件");
            throw new BizException(ErrorCodeEnum.PUB10000008);
        }

        List<DeviceExcelDto> list = null;
        try {
            list = EasyExcelUtil.readExcelWithModel(file.getInputStream(), DeviceExcelDto.class, ExcelTypeEnum.XLSX);
            if (PublicUtil.isEmpty(list)) {
                return WrapMapper.error("上传文件为空");
            }
            logger.warn("---------------------> {}", list.size());
            Map<String, Long> collect = list.parallelStream().collect(Collectors.groupingBy(DeviceExcelDto::getDeviceId, Collectors.counting()));
            List<String> result = collect.entrySet().stream()
                    .filter(e -> e.getValue() > 1).map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (null != result && !result.isEmpty()) {
                List<DeviceExcelDto> same = list.parallelStream().filter(d -> result.contains(d.getDeviceId())).collect(Collectors.toList());
                return WrapMapper.wrap(400, "设备重复添加", same);
            }
            List<SmokeDevice> devices = deviceMapper.selectList(new QueryWrapper<SmokeDevice>());
            if (PublicUtil.isNotEmpty(devices)) {
                List<DeviceExcelDto> dtos = list.parallelStream().filter(d -> devices.contains(d.getDeviceId())).collect(Collectors.toList());
                if (PublicUtil.isNotEmpty(dtos)) {
                    return WrapMapper.wrap(400, "设备重复添加", dtos.stream().map(DeviceExcelDto::getDeviceId).collect(Collectors.toList()));
                }
            }
            list.forEach(d -> {
                SmokeDevice device = new ModelMapper().map(d, SmokeDevice.class);
                device.setId(GlobalUtils.generateId());
                device.setState(0);
                device.setCreatedTime(LocalTimeUtils.LocalDateTimeToString());
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", device.getDeviceName());
                List<String> s = new ArrayList<>();
                s.add("china");
                s.add("mobile");
                map.put("tags", s);
                map.put("protocol", "LWM2M");
                map.put("private", "true");
                map.put("obsv", "true");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(d.getDeviceId(), device.getId().toString());
                map.put("auth_info", jsonObject);
                String doPost = HttpUtils.DO_POST(OneNetUrlConfig.ADD_DEVICE, new Gson().toJson(map), header, null);
                String code = new JsonParser().parse(doPost).getAsJsonObject().get("error").getAsString();
                logger.warn("OneNet添加设备请求 -------------------------> {}", code);
                if (!"succ".equals(code)) {
                    throw new BizException(ErrorCodeEnum.PUB10000009);
                }
                String returnId = new JsonParser().parse(doPost).getAsJsonObject().get("data").getAsJsonObject().get("device_id").getAsString();
                device.setReturnId(returnId);
                int insert = deviceMapper.insert(device);
                logger.warn("本地添加设备 -------------------------> {}", insert);
            });
            return WrapMapper.ok("导入成功");
        } catch (IOException e) {
            logger.info("Excel导入失败", e);
        }
        return WrapMapper.error("导入失败");
    }

    @Override
    public Wrapper deleteDeviceById(Long id) {
        if (id != null) {
            SmokeDevice device = deviceMapper.selectById(id);
            if (PublicUtil.isNotEmpty(device)) {
                if (0 != device.getState()) {
                    return WrapMapper.error("此设备已关联学校，不可删除！");
                }
            }
            if (device.getReturnId() != null) {
                String doDelete = HttpUtils.DO_DELETE(OneNetUrlConfig.DELETE_DEVICE + device.getReturnId(), header, null);
                String code = new JsonParser().parse(doDelete).getAsJsonObject().get("error").getAsString();
                logger.warn("OneNet删除设备请求 -------------------------> {}", code);
                if (!"succ".equals(code)) {
                    throw new BizException(ErrorCodeEnum.PUB10000010);
                }
            }
            deviceMapper.deleteById(id);
            return WrapMapper.ok("删除成功");
        }
        return WrapMapper.error();
    }

    @Override
    public Wrapper<DeviceVo> getInfoById(Long id) {
        if (null != id) {
            SmokeDevice device = deviceMapper.selectById(id);
            if (PublicUtil.isNotEmpty(device)) {
                DeviceVo vo = new ModelMapper().map(device, DeviceVo.class);
                return WrapMapper.ok(vo);
            }
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<DeviceVo> editInfoById(DeviceDto deviceDto) {
        if (PublicUtil.isNotEmpty(deviceDto)) {
            SmokeDevice device = new ModelMapper().map(deviceDto, SmokeDevice.class);
            deviceMapper.updateById(device);
            return WrapMapper.ok();
        }
        return WrapMapper.error("参数不能为空");
    }

    @Override
    public PageWrapper<List<DeviceListVo>> listDevices(Integer type, String deviceId, BaseQueryDto baseQueryDto) {
        QueryWrapper<SmokeDevice> wrapper = null;
        if (0 == type) {
            if (null != deviceId) {
                wrapper = new QueryWrapper<SmokeDevice>().like("device_id", deviceId).orderByDesc("created_time");
            } else {
                wrapper = new QueryWrapper<SmokeDevice>().eq("state", 0).orderByDesc("created_time");
            }
        } else {
            if (null != deviceId) {
                wrapper = new QueryWrapper<SmokeDevice>().like("device_id", deviceId).eq("state", 1).orderByDesc("created_time");
            } else {
                wrapper = new QueryWrapper<SmokeDevice>().eq("state", 1).orderByDesc("created_time");
            }
        }
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SmokeDevice> devices = deviceMapper.selectList(wrapper);
        Long total = page.getTotal();
        if (PublicUtil.isNotEmpty(devices)) {
            List<DeviceListVo> list = new ArrayList<>();
            devices.forEach(device -> {
                DeviceListVo map = new ModelMapper().map(device, DeviceListVo.class);
                String doGet = HttpUtils.DO_GET(OneNetUrlConfig.GET_DEVICE_STATUS + device.getReturnId(), header, null);
                String code = new JsonParser().parse(doGet).getAsJsonObject().get("error").getAsString();
                logger.warn("OneNet获取设备状态 -------------------------> {}", code);
                if (!"succ".equals(code)) {
                    throw new BizException(ErrorCodeEnum.PUB10000011);
                }
                String online = new JsonParser().parse(doGet).getAsJsonObject().get("data").getAsJsonObject().get("online").getAsString();
                map.setOnline(online);
                list.add(map);
            });
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        }
        return PageWrapMapper.wrap(400, "暂无数据");
    }

    @Override
    public Wrapper associateById(Integer type, Long id, Long masterId, String masterName) {
        if (null != type && null != id) {
            SmokeDevice device = deviceMapper.selectById(id);
            if (PublicUtil.isNotEmpty(device)) {
                if (0 == type) {
                    device.setMasterId(null);
                    device.setMasterName(null);
                    device.setState(0);
                    deviceMapper.updateById(device);
                } else {
                    if (null == masterId && null == masterName) {
                        return WrapMapper.error("参数不能为空");
                    }
                    device.setMasterId(masterId);
                    device.setMasterName(masterName);
                    device.setState(1);
                    deviceMapper.updateById(device);
                }
                return WrapMapper.ok("操作成功");
            }
        }
        return WrapMapper.error("操作失败");
    }

    @Override
    public Wrapper<List<DeviceListVo>> deviceCodeList() {
        List<SmokeDevice> devices = deviceMapper.selectList(new QueryWrapper<SmokeDevice>().eq("state", 0).orderByDesc("created_time"));
        if (PublicUtil.isNotEmpty(devices)) {
            List<DeviceListVo> list = new ArrayList<>();
            devices.forEach(d -> {
                if (0 == d.getState()) {
                    DeviceListVo map = new ModelMapper().map(d, DeviceListVo.class);
                    list.add(map);
                }
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<List<SmokeData>> dataList(BaseQueryDto baseQueryDto) {
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SmokeData> datas = dataMapper.selectList(new QueryWrapper<SmokeData>().orderByDesc("report_time"));
        Long total = page.getTotal();
        return PageWrapMapper.wrap(datas, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
    }
}
