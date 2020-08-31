package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BuildingStudent {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "楼幢ID")
    private Long noId;

    @ApiModelProperty(value = "房间ID")
    private Long levelId;

    @ApiModelProperty(value = "房间ID")
    private Long roomId;

    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    @ApiModelProperty(value = "床位")
    private Long bedId;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
