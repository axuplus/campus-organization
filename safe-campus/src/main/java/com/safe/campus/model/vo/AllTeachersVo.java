package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AllTeachersVo {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("工号")
    private String tNumber;
    @ApiModelProperty("性别 1:男 0：女")
    private Integer sex;
    @ApiModelProperty("身份证号码")
    private String idNumber;
    @ApiModelProperty("电话")
    private Long phone;
    @ApiModelProperty("照片")
    private String img;
    @ApiModelProperty("部门ID")
    private Long sectionId;
    @ApiModelProperty("部门名称")
    private String sectionName;
    @ApiModelProperty("职务")
    private String position;
    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "班级ID")
    private Long classInfoId;
    @ApiModelProperty(value = "班级名称")
    private String classInfoName;
}
