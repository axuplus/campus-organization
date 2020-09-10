package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "班级节点渲染")
public class NodeTreeVo {
    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "1 类型")
    private Integer type;
    @ApiModelProperty(value = "年级名称")
    private String className;
    private List<SubClass> subClassInfo;

    @Data
    public static class SubClass{
        @ApiModelProperty(value = "班级id")
        private Long subClassId;
        @ApiModelProperty(value = "2 类型")
        private Integer type;
        @ApiModelProperty(value = "班级名称")
        private String subClassName;
    }
}
