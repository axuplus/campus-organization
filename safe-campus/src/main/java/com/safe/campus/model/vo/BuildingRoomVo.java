package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Data
public class BuildingRoomVo {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "学生ID")
    private Long studentId;
    @ApiModelProperty(value = "姓名")
    private String sName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty("学号")
    private String sNumber;
    @ApiModelProperty("楼幢号码")
    private String buildingNo;
    @ApiModelProperty(value = "楼层")
    private Integer buildingLevel;
    @ApiModelProperty(value = "宿舍")
    private Integer buildingRoom;
    @ApiModelProperty(value = "床位")
    private Integer bedNo;
}
