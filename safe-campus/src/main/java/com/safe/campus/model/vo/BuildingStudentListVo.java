package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildingStudentListVo {
    @ApiModelProperty(value = "building student id")
    private String id;
    @ApiModelProperty(value = "bedId")
    private Long bedId;
    @ApiModelProperty(value = "楼幢")
    private String buildingNo;
    @ApiModelProperty(value = "楼幢层")
    private String buildingLevel;
    @ApiModelProperty(value = "宿舍")
    private String buildingRoom;
    @ApiModelProperty(value = "床号")
    private String bedNo;
    @ApiModelProperty(value = "学生姓名")
    private String sName;
    @ApiModelProperty(value = "年级+班级")
    private String classInfo;
    @ApiModelProperty(value = "学生号")
    private String sNumber;
    @ApiModelProperty(value = "有无学生 1有人 0无人")
    private Integer state;
}
