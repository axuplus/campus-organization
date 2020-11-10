package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolStudentListVo {

    @ApiModelProperty(value = "姓名")
    private String sName;
    @ApiModelProperty(value = "id")
    private  String id;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "班级名称")
    private String classInfoName;
    @ApiModelProperty(value = "学号")
    private String sNumber;
    @ApiModelProperty(value = "身份证")
    private String idNumber;
    @ApiModelProperty("类型")
    private Integer type;
}
