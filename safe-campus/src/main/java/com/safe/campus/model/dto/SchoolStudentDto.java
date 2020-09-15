package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolStudentDto {
    @ApiModelProperty(value = "id",required = false)
    private Long id;
    @ApiModelProperty(value = "学校ID",required = true)
    private Long masterId;
    @ApiModelProperty(value = "学生姓名", required = true)
    private String sName;
    @ApiModelProperty(value = "学号", required = true)
    private String sNumber;
    @ApiModelProperty(value = "性别1：男 0：女", required = true)
    private Integer sex;
    @ApiModelProperty(value = "身份证号码", required = true)
    private String idNumber;
    @ApiModelProperty(value = "1：住校生 2：同校生", required = true)
    private Integer type;
    @ApiModelProperty(value = "楼幢ID")
    private Long buildingNoId;
    @ApiModelProperty(value = "楼层ID")
    private Long buildingLevelId;
    @ApiModelProperty(value = "房间ID")
    private Long buildingRoomId;
    @ApiModelProperty(value = "床位ID")
    private Long buildingBedNoId;
    @ApiModelProperty(value = "图片ID")
    private Long imgId;
    @ApiModelProperty(value = "年级ID", required = true)
    private Long classId;
    @ApiModelProperty(value = "年级", required = true)
    private String className;
    @ApiModelProperty(value = "班级ID", required = true)
    private Long classInfoId;
    @ApiModelProperty(value = "班级", required = true)
    private String classInfoName;
    @ApiModelProperty(value = "入学年月")
    private String joinTime;
    @ApiModelProperty(value = "毕业年份")
    private String endTime;
}
