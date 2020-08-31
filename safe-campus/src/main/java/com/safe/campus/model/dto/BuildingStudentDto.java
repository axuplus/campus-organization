package com.safe.campus.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildingStudentDto {
    @ApiModelProperty("楼幢Id")
    private Long buildingNoId;
    @ApiModelProperty("楼层ID")
    private Long buildingLevelId;
    @ApiModelProperty("房间ID")
    private Long buildingRoomId;
    @ApiModelProperty("床位ID")
    private Long bedId;
    @ApiModelProperty("学生ID")
    private Long studentId;
}
