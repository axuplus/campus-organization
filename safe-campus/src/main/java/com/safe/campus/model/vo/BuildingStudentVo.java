package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildingStudentVo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "学生名称")
    private String sName;
    @ApiModelProperty(value = "学生编号")
    private String sNumber;
    @ApiModelProperty(value = "学生性别")
    private String sex;
}
