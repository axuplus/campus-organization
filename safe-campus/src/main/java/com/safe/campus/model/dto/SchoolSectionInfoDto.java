package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolSectionInfoDto {

    @ApiModelProperty(value = "id", name = "id", required = true)
    private Long id;
    @ApiModelProperty(value = "上级部门", name = "pId", required = true)
    private Long pid;
    @ApiModelProperty(value = "当前pid是否是学校id 1是0否", name = "当前pid是否是学校id", required = true)
    private Integer type;
    @ApiModelProperty(value = "部门名称", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "负责人ID", name = "tId", required = false)
    private Long tId;

}
