package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassDto {

    @ApiModelProperty(value = "如果是新增就不用管，如果是编辑就传id", name = "id", required = false)
    private Long id;
    @ApiModelProperty(value = "年级主任ID/如果是新增就不用管，如果是编辑就传id", name = "directorId", required = false)
    private Long tId;
    @ApiModelProperty(value = "学校ID", name = "masterId", required = true)
    private Long  masterId;
    @ApiModelProperty(value = "年级名称", name = "className", required = true)
    private String className;
    @ApiModelProperty(value = "0：新增 1：编辑", name = "type", required = true)
    private Integer type;
}
