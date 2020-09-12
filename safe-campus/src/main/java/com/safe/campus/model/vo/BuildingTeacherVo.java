package com.safe.campus.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Map;

@Data
public class BuildingTeacherVo {
    @ApiModelProperty(value = "楼幢ID")
    private Long buildingId;
    @ApiModelProperty(value = "楼幢编号")
    private String buildingNo;
    @ApiModelProperty(value = "楼层Id")
    private Long levelId;
    @ApiModelProperty(value = "楼层")
    private String level;
    @ApiModelProperty(value = "宿管老师")
    private String tName;
}
