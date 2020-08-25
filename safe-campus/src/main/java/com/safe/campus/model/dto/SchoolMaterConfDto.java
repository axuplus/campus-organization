package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SchoolMaterConfDto {
    @ApiModelProperty(value = "masterId", required = true)
    private Long masterId;
    @ApiModelProperty(value = "数组", required = true)
    private List<Long> routeId;
}
