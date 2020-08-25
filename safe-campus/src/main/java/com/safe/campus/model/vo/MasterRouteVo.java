package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MasterRouteVo {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "模块名称")
    private String moduleName;
}
