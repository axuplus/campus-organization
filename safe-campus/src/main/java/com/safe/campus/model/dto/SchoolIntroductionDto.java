package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SchoolIntroductionDto {

    @ApiModelProperty(value = "学校id")
    private Long masterId;
    @ApiModelProperty(value = "描述")
    private String introduction;
    @ApiModelProperty(value = "轮播图 url")
    private List<String> imgs;
}
