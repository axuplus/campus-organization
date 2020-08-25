package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolSectionInfoDto {

    @ApiModelProperty(value = "id", name = "pId", required = true)
    private Long id;
    @ApiModelProperty(value = "部门名称", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "负责人ID", name = "tId", required = true)
    private Long tId;

}
