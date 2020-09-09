package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SectionVo {
    @ApiModelProperty(value = "部门id")
    private Long sectionId;
    @ApiModelProperty(value = "1是学校 0是部门")
    private Integer type;
    @ApiModelProperty(value = "部门名称")
    private String sectionName;
}
