package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SchoolTeacherVo {
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
    private String photo;
    @ApiModelProperty("部门名称")
    private String sectionName;
    @ApiModelProperty("部门ID")
    private Long sectionId;
    @ApiModelProperty("职务")
    private String position;
    @ApiModelProperty("关联班级")
    private String classInformation;
    @ApiModelProperty("角色信息 map")
    private Map<Long, String> roleInfo;
}
