package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveBuildingInfoDto {

    @ApiModelProperty(value = "学校id")
    private Long masterId;
    @ApiModelProperty(value = "类型 1楼幢 2楼层 3房间 4床位")
    private Integer type;
    @ApiModelProperty(value = "type为1时不用传这个id type为其他时传上面的id 比如type为2就给我楼幢的id")
    private Long id;
    @ApiModelProperty(value = "名字")
    private String name;
}
