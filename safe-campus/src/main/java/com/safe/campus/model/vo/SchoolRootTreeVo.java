package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SchoolRootTreeVo {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "level")
    private Integer level;
    @ApiModelProperty(value = "名称")
    private String rootName;
    @ApiModelProperty(value = "pid")
    private Long pid;
    @ApiModelProperty(value = "子集")
    private List<SchoolRootTreeVo> children;
}
