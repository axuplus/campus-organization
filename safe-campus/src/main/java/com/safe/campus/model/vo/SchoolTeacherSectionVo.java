package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolTeacherSectionVo {
    @ApiModelProperty(value = "部门id")
    private Long sectionId;
    @ApiModelProperty(value = "部门名称")
    private String sectionName;
}
