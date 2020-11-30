package com.smoke.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.smoke.service.config.OneNetUrlConfig;
import com.smoke.service.mapper.SmokeDataMapper;
import com.smoke.service.mapper.SmokeDeviceMapper;
import com.smoke.service.model.dto.DataTran;
import com.smoke.service.model.entity.SmokeData;
import com.smoke.service.model.entity.SmokeDevice;
import com.smoke.service.service.SmokeDataService;
import com.smoke.service.utils.GlobalUtils;
import com.smoke.service.utils.HttpUtils;
import com.smoke.service.utils.LocalTimeUtils;
import com.smoke.service.utils.PublicUtil;
import com.smoke.service.utils.wrapper.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.time.LocalDateTime;
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
public class SmokeDataServiceImpl extends ServiceImpl<SmokeDataMapper, SmokeData> implements SmokeDataService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static Map header = new HashMap();

    static {
        header.put("api-key", "EEaWTfScdgkPYE1MxLfN=BV=a60=");
    }

    @Autowired
    private SmokeDataMapper dataMapper;

    @Autowired
    private SmokeDeviceMapper deviceMapper;

    @Scheduled(cron = "0/30 * *  * * ? ")   //每10秒执行一次
    public void synchronizeData() throws Exception {
        List<SmokeDevice> devices = deviceMapper.selectList(new QueryWrapper<SmokeDevice>().eq("state", 1));
        if (PublicUtil.isNotEmpty(devices)) {
            List<String> ids = devices.stream().map(SmokeDevice::getReturnId).collect(Collectors.toList());
            String str = ids.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
            String doGet = HttpUtils.DO_GET(OneNetUrlConfig.BATCH_DATA + str, header);
            String error = new JsonParser().parse(doGet).getAsJsonObject().get("error").getAsString();
            if (!"succ".equals(error)) {
                logger.warn("定时同步数据失败 {}", error);
            }
            String array = new JsonParser().parse(doGet)
                    .getAsJsonObject()
                    .get("data")
                    .getAsJsonObject()
                    .get("devices")
                    .getAsJsonArray().toString();
            List<DataTran> list = new Gson().fromJson(array, new TypeToken<List<DataTran>>() {
            }.getType());
            if (PublicUtil.isNotEmpty(list)) {
                list.forEach(data -> {
                    logger.info("数据-------------------------->{}", data);
                    if (null != data.getDatastreams() && !data.getDatastreams().isEmpty()) {
                        DataTran.DataStream dataStream = data.getDatastreams().get(0);
                        List<String> strings = dataStream.getValue().subList(26, 27);
                        Integer state = Integer.valueOf(strings.get(0));
                        SmokeData smokeData = dataMapper.selectOne(new QueryWrapper<SmokeData>().eq("return_id", data.getId()).orderByDesc("report_time").last("limit 0,1"));
                        if (PublicUtil.isNotEmpty(smokeData)) {
                            LocalDateTime newDate = LocalTimeUtils.StringToLocalDateTime(dataStream.getAt());
                            LocalDateTime oldDate = LocalTimeUtils.StringToLocalDateTime(smokeData.getReportTime());
                            if (oldDate.isBefore(newDate)) {
                                SmokeData smoke = new SmokeData();
                                smoke.setId(GlobalUtils.generateId());
                                smoke.setReturnId(data.getId());
                                smoke.setState(state);
                                smoke.setDeviceLocation(smokeData.getDeviceLocation());
                                smoke.setDeviceName(smokeData.getDeviceName());
                                smoke.setMasterId(smokeData.getMasterId());
                                smoke.setReportTime(dataStream.getAt());
                                logger.warn("插入-------------------------->{}", smoke.getReportTime());
                                dataMapper.insert(smoke);
                            }
                        } else {
                            SmokeData smoke = new SmokeData();
                            smoke.setId(GlobalUtils.generateId());
                            smoke.setReturnId(data.getId());
                            smoke.setState(state);
                            SmokeDevice smokeDevice = deviceMapper.selectOne(new QueryWrapper<SmokeDevice>().eq("return_id", data.getId()));
                            smoke.setDeviceLocation(smokeDevice.getDeviceLocation());
                            smoke.setDeviceName(smokeDevice.getDeviceName());
                            smoke.setMasterId(smokeDevice.getMasterId());
                            smoke.setReportTime(dataStream.getAt());
                            logger.warn("!插入{}------------------>", smoke.getReportTime());
                            dataMapper.insert(smoke);
                        }
                    }
                });
            }
        }

    }

    @Override
    public Wrapper saveResponse(String response) {
        if (null != response) {
            logger.info("收到的数据是 {}", response);
        }
        return null;
    }

    @Override
    public String test(String msg, String nonce, String signature) {
        return msg;
    }
}



