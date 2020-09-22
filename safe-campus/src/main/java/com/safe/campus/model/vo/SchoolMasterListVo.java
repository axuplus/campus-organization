package com.safe.campus.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SchoolMasterListVo {

    @ApiModelProperty("学校名称")
    private  Long id;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("账号")
    private String appKey;
    @ApiModelProperty("到期时间")
    private String endTime;
    @ApiModelProperty("状态 1: 已禁用 0：未禁用")
    private Integer state;

}
