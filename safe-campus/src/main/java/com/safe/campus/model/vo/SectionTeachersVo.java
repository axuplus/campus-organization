package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SectionTeachersVo {
    private Long tId;
    private String tName;
    @ApiModelProperty(value = "部门名称")
    private String sectionName;
}
