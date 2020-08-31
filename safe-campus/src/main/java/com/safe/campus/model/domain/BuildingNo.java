package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 楼幢表
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BuildingNo对象", description = "楼幢表")
public class BuildingNo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "学校ID")
    private Long masterId;

    @ApiModelProperty(value = "楼幢信息")
    private String buildingNo;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "是否删除1：已删除 0：未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
