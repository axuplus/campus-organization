package com.safe.campus.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginTokenDto implements java.io.Serializable {


    @ApiModelProperty(value = "id", name = "id")
    private Long id;
    @ApiModelProperty(value = "type", name = "type")
    private Integer type;
    @ApiModelProperty(value = "masterId", name = "masterId")
    private Long masterId;
    @ApiModelProperty(value = "名称", name = "userName")
    private String userName;
    @ApiModelProperty(value = "状态 0：禁用，1：正常", name = "state")
    private Integer state;
    @ApiModelProperty(value = "创建人", name = "createUser")
    private Long createUser;
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Long createTime;
    @ApiModelProperty(value = "过期时间", name = "expireIn")
    private Long expireTime;
}
