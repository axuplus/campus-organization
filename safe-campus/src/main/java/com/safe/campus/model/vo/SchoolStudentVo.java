package com.safe.campus.model.vo;

import com.safe.campus.model.dto.BuildingBedDto;
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
    private String photo;
    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "年级")
    private String className;
    @ApiModelProperty(value = "班级ID")
    private Long classInfoId;
    @ApiModelProperty(value = "班级")
    private String classInfoName;
    @ApiModelProperty(value = "入学年份")
    private String joinTime;
    @ApiModelProperty(value = "毕业年份")
    private String endTime;
    @ApiModelProperty(value = "住宿信息")
    private BuildingBedDto livingInfo;
}
