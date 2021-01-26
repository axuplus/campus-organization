package com.smoke.service.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceListVo {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "0：未关联 1：已关联")
    private Integer state;
    @ApiModelProperty(value = "false：离线 true：在线")
    private String online;
    @ApiModelProperty(value = "返回ID")
    private String returnId;
    @ApiModelProperty(value = "绑定学校")
    private String masterName;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备厂家")
    private String deviceFactory;
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;
    @ApiModelProperty(value = "安装位置")
    private String deviceLocation;
}
