package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassInfoDto {
    @ApiModelProperty(value = "如果是新增就不用管，如果是编辑就传id", name = "id", required = false)
    private Long id;
    @ApiModelProperty(value = "年级ID", name = "classId", required = true)
    private Long classId;
    @ApiModelProperty(value = "年级名称", name = "classInfoName", required = true)
    private String classInfoName;
    @ApiModelProperty(value = "0：新增 1：编辑", name = "type", required = true)
    private Integer type;
    @ApiModelProperty(value = "如果是新增就不用管，如果是编辑就传班主任ID", name = "superiorId", required = false)
    private Long superiorId;
}
