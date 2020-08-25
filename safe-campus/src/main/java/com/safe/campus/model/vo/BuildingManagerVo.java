package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildingManagerVo {


    @ApiModelProperty(value = "楼幢ID")
    private Long buildingId;
    @ApiModelProperty(value = "楼幢")
    private String buildingNo;
    @ApiModelProperty(value = "楼层")
    private Integer buildingLevel;
    @ApiModelProperty(value = "管理员Id")
    private Long managerId;
    @ApiModelProperty(value = "管理员名称")
    private String managerName;
    @ApiModelProperty(value = "管理员编号")
    private String managerNo;
    @ApiModelProperty(value = "管理员电话")
    private Long managerPhone;
}
