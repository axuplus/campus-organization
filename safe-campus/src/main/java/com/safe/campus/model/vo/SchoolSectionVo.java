package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SchoolSectionVo {

    @ApiModelProperty(value = "sectionId", name = "sectionId")
    private Long sectionId;
    @ApiModelProperty(value = "名称", name = "sectionName")
    private String sectionName;
    @ApiModelProperty(value = "上级", name = "preSectionName")
    private String preSectionName;
    @ApiModelProperty(value = "负责人电话", name = "name")
    private String name;
    @ApiModelProperty(value = "负责人ID", name = "tId")
    private Long tId;
    @ApiModelProperty(value = "手机", name = "phone")
    private Long phone;
    @ApiModelProperty(value = "状态", name = "state")
    private Integer state;

}
