package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentDocVo {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "学号")
    private String sNumber;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "班级名称")
    private String classInfoName;
    @ApiModelProperty(value = "身份证")
    private String sIdNumber;
    @ApiModelProperty(value = "1：住校生 2：同校生")
    private Integer type;
    @ApiModelProperty(value = "图片")
    private String img;
    @ApiModelProperty(value = "入学年月")
    private String joinTime;
    @ApiModelProperty(value = "楼幢")
    private String buildingNo;
    @ApiModelProperty(value = "楼层")
    private String buildingLevel;
    @ApiModelProperty(value = "宿舍")
    private String buildingRoom;
    @ApiModelProperty(value = "床位")
    private String buildingBed;
}
