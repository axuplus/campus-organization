package com.smoke.service.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Joma
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SmokeDevice对象", description="")
public class SmokeDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long masterId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "0：未关联 1：已关联")
    private Boolean state;

    @ApiModelProperty(value = "0：下线 1：在线")
    private Boolean shape;

    @ApiModelProperty(value = "设备厂家")
    private String deviceFactory;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "安装位置")
    private String deviceLocation;

    @ApiModelProperty(value = "添加时间")
    private Date createdTime;


}
