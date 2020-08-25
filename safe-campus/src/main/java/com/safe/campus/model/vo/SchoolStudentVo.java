package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolStudentVo {

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "学生姓名")
    private String sName;
    @ApiModelProperty(value = "学号")
    private String sNumber;
    @ApiModelProperty(value = "性别1：男 0：女")
    private String sex;
    @ApiModelProperty(value = "身份证号码")
    private String idNumber;
    @ApiModelProperty(value = "1：住校生 2：同校生")
    private String type;
    @ApiModelProperty(value = "图片ID")
    private Long imgId;
    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "年级")
    private String className;
    @ApiModelProperty(value = "班级ID")
    private Long classInfoId;
    @ApiModelProperty(value = "班级")
    private String classInfoName;
}
