package com.smoke.service.controller;

import com.smoke.service.model.dto.AssociateDto;
import com.smoke.service.model.dto.DeviceDto;
import com.smoke.service.model.entity.SmokeData;
import com.smoke.service.model.vo.DeviceListVo;
import com.smoke.service.model.vo.DeviceVo;
import com.smoke.service.service.SmokeDataService;
import com.smoke.service.service.SmokeDeviceService;
import com.smoke.service.utils.wrapper.BaseQueryDto;
import com.smoke.service.utils.wrapper.PageWrapper;
import com.smoke.service.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/smoke")
@Api(value = "烟感接口", tags = {"烟感接口"})
public class SmokeController {

    @Autowired
    private SmokeDeviceService deviceService;

    @Autowired
    private SmokeDataService dataService;


    @ApiOperation("添加设备（总）")
    @PostMapping("/addDevice")
    public Wrapper addDevice(@RequestBody DeviceDto deviceDto) {
        return deviceService.addDevice(deviceDto);
    }

    @ApiOperation("批量添加设备（总）")
    @PostMapping("/batchAddDevices")
    public Wrapper batchAddDevices(@ApiParam(value = "file", required = true) MultipartFile file) {
        return deviceService.batchAddDevices(file);
    }

    @ApiOperation("删除设备 （总）")
    @DeleteMapping("/delete/{id}")
    public Wrapper deleteDeviceById(@PathVariable("id") Long id) {
        return deviceService.deleteDeviceById(id);
    }

    @ApiOperation("编辑设备 （总）")
    @PutMapping("/editInfoById")
    public Wrapper<DeviceVo> editInfoById(@RequestBody DeviceDto deviceDto) {
        return deviceService.editInfoById(deviceDto);
    }

    @ApiOperation("查看设备")
    @GetMapping("/getInfoById")
    public Wrapper<DeviceVo> getInfoById(@RequestParam("id") Long id) {
        return deviceService.getInfoById(id);
    }

    @ApiOperation("搜索&列表（传值搜索 否则列表）")
    @GetMapping("/listDevices")
    public PageWrapper<List<DeviceListVo>> listDevices(@ApiParam("0 总后台 1学校")@RequestParam("type")Integer type,@RequestParam("masterId") Long masterId, @RequestParam(value = "deviceId", required = false) String deviceId, BaseQueryDto baseQueryDto) {
        return deviceService.listDevices(type,masterId, deviceId, baseQueryDto);
    }

    @ApiOperation("关联/取消 设备")
    @PostMapping("/associateByIds")
    public Wrapper associateById(@RequestBody AssociateDto associateDto) {
        return deviceService.associateById(associateDto);
    }

    @ApiOperation("设备码下拉列表")
    @GetMapping("/deviceCodeList")
    public Wrapper<List<DeviceListVo>> deviceCodeList(@RequestParam("masterId")Long masterId) {
        return deviceService.deviceCodeList(masterId);
    }


    @ApiOperation("数据列表")
    @GetMapping("/dataList")
    public Wrapper<List<SmokeData>> dataList(@RequestParam("masterId")Long masterId, BaseQueryDto baseQueryDto) {
        return deviceService.dataList(masterId,baseQueryDto);
    }

    @ApiOperation("解除告警")
    @GetMapping("/disable")
    public Wrapper disableCode(@RequestParam("id") Long id) {
        return deviceService.disableCode(id);
    }

    @PostMapping("/test")
    public Wrapper saveResponse(@RequestBody String response) {
        return dataService.saveResponse(response);
    }

    @GetMapping("/test")
    public String test(@RequestParam("msg") String msg, @RequestParam("nonce") String nonce, @RequestParam("signature") String signature) {
        return dataService.test(msg, nonce, signature);
    }
}
