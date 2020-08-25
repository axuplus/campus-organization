package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolClassVo {

    @ApiModelProperty(value = "年级ID")
    private Long classId;
    @ApiModelProperty(value = "年级名称")
    private String className;
    @ApiModelProperty(value = "年级主任名称")
    private String classSuperiorName;
    @ApiModelProperty(value = "电话")
    private Long phone;
    @ApiModelProperty(value = "状态")
    private Integer state;
}
