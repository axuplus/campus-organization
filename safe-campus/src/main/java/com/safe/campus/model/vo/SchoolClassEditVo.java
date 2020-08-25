package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassEditVo {

    @ApiModelProperty("年级Id")
    private Long classId;
    @ApiModelProperty("班级Id")
    private Long classInfoId;
    @ApiModelProperty("年级名称")
    private String className;
    @ApiModelProperty("班级名称")
    private String classInfoName;
    @ApiModelProperty("班主任/年级主任 Id")
    private Long superiorId;
    @ApiModelProperty("班主任/年级主任 姓名")
    private String superiorName;
}
