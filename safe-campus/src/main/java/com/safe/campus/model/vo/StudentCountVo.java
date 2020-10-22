package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentCountVo {
    private Long classId;
    private String className;
    private Long classInfoId;
    private String classInfoName;
    @ApiModelProperty(value = "总")
    private Integer total;
    @ApiModelProperty(value = "住")
    private Integer liveCount;
    @ApiModelProperty(value = "通")
    private Integer leaveCount;
}
