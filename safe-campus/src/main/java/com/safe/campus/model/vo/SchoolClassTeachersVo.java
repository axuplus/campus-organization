package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "配置管理人列表")
public class SchoolClassTeachersVo {
    @ApiModelProperty(value = "教师ID")
    private Long tId;
    @ApiModelProperty(value = "教师姓名")
    private String tName;
    @ApiModelProperty(value = "部门名称")
    private String sectionName;
}
