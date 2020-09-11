package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassDto {

    @ApiModelProperty(value = "学校ID", name = "masterId", required = true)
    private Long  masterId;
    @ApiModelProperty(value = "名称", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "1：添加年级 2：添加班级", name = "type", required = true)
    private Integer type;
    @ApiModelProperty(value = "年级ID", name = "classId", required = false)
    private Long classId;
}
