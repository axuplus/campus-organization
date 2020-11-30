package com.smoke.service.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@ApiModel(value="SmokeData对象", description="")
public class SmokeData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long masterId;

    @ApiModelProperty(value = "设备ID")
    private String returnId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备位置")
    private String deviceLocation;

    @ApiModelProperty(value = "0：正常 1：报警 -1已处理")
    private Integer state;

    @ApiModelProperty(value = "推送时间")
    private String reportTime;


}
