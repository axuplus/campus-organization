package com.smoke.service.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AssociateDto {
    @ApiModelProperty(value = "0取消 1关联")
    private Integer type;
    @ApiModelProperty(value = "deviceIds")
    private List<Long> ids;
    @ApiModelProperty(value = "学校ID")
    private Long masterId;
}
