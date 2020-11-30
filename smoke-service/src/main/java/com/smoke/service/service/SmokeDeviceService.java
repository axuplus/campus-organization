package com.smoke.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smoke.service.model.dto.DeviceDto;
import com.smoke.service.model.entity.SmokeData;
import com.smoke.service.model.vo.DeviceListVo;
import com.smoke.service.model.vo.DeviceVo;
import com.smoke.service.model.entity.SmokeDevice;
import com.smoke.service.utils.wrapper.BaseQueryDto;
import com.smoke.service.utils.wrapper.PageWrapper;
import com.smoke.service.utils.wrapper.Wrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Joma
 * @since 2020-11-12
 */
public interface SmokeDeviceService extends IService<SmokeDevice> {

    Wrapper addDevice(DeviceDto deviceDto);

    Wrapper batchAddDevices(MultipartFile file);

    Wrapper deleteDeviceById(Long id);

    Wrapper<DeviceVo> getInfoById(Long id);

    Wrapper<DeviceVo> editInfoById(DeviceDto deviceDto);

    PageWrapper<List<DeviceListVo>> listDevices(Integer type,String deviceId, BaseQueryDto baseQueryDto);

    Wrapper associateById(Integer type, Long id, Long masterId, String masterName);

    Wrapper<List<DeviceListVo>> deviceCodeList();

    Wrapper<List<SmokeData>> dataList(BaseQueryDto baseQueryDto);

    Wrapper disableCode(Long id);
}
