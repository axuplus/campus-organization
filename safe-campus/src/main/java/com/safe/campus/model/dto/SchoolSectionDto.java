package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SchoolSectionDto", description = "部门信息")
public class SchoolSectionDto {


    @ApiModelProperty(value = "部门名称", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "是否是根节点 1：是 0：否", name = "isRoot", required = true)
    private Integer isRoot;
    @ApiModelProperty(value = "学校ID", name = "masterId", required = true)
    private Long masterId;
    @ApiModelProperty(value = "如果不是校区根节点的话就传", name = "sectionId", required = false)
    private Long sectionId;
    @ApiModelProperty(value = "配置负责人", name = "tId", required = false)
    private Long tId;
}
