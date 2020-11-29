package com.smoke.service.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "返回ID")
    private String returnId;

    @ApiModelProperty(value = "学校ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long masterId;

    @ApiModelProperty(value = "学校名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String masterName;

    @ApiModelProperty(value = "0：未关联 1：已关联")
    private Integer state;

    @ApiModelProperty(value = "设备厂家")
    private String deviceFactory;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "安装位置")
    private String deviceLocation;

    @ApiModelProperty(value = "添加时间")
    private String createdTime;


}
