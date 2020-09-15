package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassStudentVo {
    @ApiModelProperty(value = "type为1是年级id 2为班级id")
    private Long id;
    @ApiModelProperty(value = "1位年级 2为班级")
    private Integer type;
    @ApiModelProperty(value = "年级或班级名称")
    private String name;
}
