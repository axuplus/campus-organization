package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassSearchVo {

    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "班级ID")
    private Long classInfoId;
    @ApiModelProperty(value = "班级名称")
    private String classInfoName;
    @ApiModelProperty(value = "年级主任/班主任名称")
    private String classSuperiorName;
    @ApiModelProperty(value = "电话")
    private Long phone;
    @ApiModelProperty(value = "状态")
    private Integer state;
    @ApiModelProperty(value = "类型 1：年级 2：班级")
    private Integer type;
}
