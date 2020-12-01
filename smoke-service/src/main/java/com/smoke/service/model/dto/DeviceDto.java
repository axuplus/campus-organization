package com.smoke.service.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceDto {
    @ApiModelProperty(value = "id (修改用)", required = false)
    private Long id;
    @ApiModelProperty(value = "设备ID", required = true)
    private String deviceId;
    @ApiModelProperty(value = "学校ID", required = true)
    private Long masterId;
    @ApiModelProperty(value = "学校名称")
    private String masterName;
    @ApiModelProperty(value = "设备名称", required = true)
    private String deviceName;
    @ApiModelProperty(value = "设备厂家", required = true)
    private String deviceFactory;
    @ApiModelProperty(value = "设备型号", required = true)
    private String deviceModel;
    @ApiModelProperty(value = "安装位置", required = true)
    private String deviceLocation;
}
