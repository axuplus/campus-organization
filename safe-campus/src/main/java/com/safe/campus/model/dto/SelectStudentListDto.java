package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectStudentListDto {
    private Long studentId;
    private Integer type;
    @ApiModelProperty(value = "年级id")
    private Long classId;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "班级id")
    private Long classInfoId;
    @ApiModelProperty(value = "班级名称")
    private String classInfoName;
}
