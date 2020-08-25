package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SchoolSectionVo {

    @ApiModelProperty(value = "sectionId", name = "sectionId", required = true)
    private Long sectionId;
    @ApiModelProperty(value = "名称", name = "sectionName", required = true)
    private String sectionName;
    @ApiModelProperty(value = "上级", name = "preSectionName", required = true)
    private String preSectionName;
    @ApiModelProperty(value = "负责人电话", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "负责人ID", name = "tId", required = true)
    private Long tId;
    @ApiModelProperty(value = "手机", name = "phone", required = true)
    private Long phone;
    @ApiModelProperty(value = "状态", name = "state", required = true)
    private Integer state;

}
