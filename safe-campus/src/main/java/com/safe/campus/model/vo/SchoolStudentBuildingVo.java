package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolStudentBuildingVo {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "楼幢或楼层或宿舍或床位")
    private String name;
    @ApiModelProperty(value = "类型 1楼幢 2楼层 3宿舍 4床位")
    private Integer type;
    @ApiModelProperty(value = "床位状态 1 有人 0 无人（只有type为4的时候有值）")
    private Integer state;
}
