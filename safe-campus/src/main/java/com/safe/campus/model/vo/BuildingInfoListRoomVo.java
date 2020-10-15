package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildingInfoListRoomVo {
    @ApiModelProperty(value = "楼幢")
    private String buildingNo;
    @ApiModelProperty(value = "楼层")
    private String buildingLevel;
    @ApiModelProperty(value = "宿舍id")
    private Long roomId;
    @ApiModelProperty(value = "宿舍名称")
    private String roomName;
    @ApiModelProperty(value = "床位统计")
    private Integer bedCount;
    @ApiModelProperty(value = "学生统计")
    private Integer studentCount;
}
