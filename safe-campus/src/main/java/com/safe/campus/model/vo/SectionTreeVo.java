package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SectionTreeVo {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "层级")
    private Integer level;
//    @ApiModelProperty(value = "不要管这个parentName")
//    private String parentName;
    @ApiModelProperty(value = "部门名称")
    private String sectionName;
    @ApiModelProperty(value = "pid")
    private Long pid;
    @ApiModelProperty(value = "子集")
    private List<SectionTreeVo> children;
}
