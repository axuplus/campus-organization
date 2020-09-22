package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveOrEditNodeDto {

    @ApiModelProperty(value = "1添加 2修改", required = true)
    private Integer type;
    @ApiModelProperty(value = "修改传值", required = true)
    private Long id;
    @ApiModelProperty(value = "节点名称", required = true)
    private String nodeName;
}
