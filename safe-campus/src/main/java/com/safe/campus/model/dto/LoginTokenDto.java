package com.safe.campus.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginTokenDto implements java.io.Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id", name = "id")
    private Long id;


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "pwd", required = true)
    private String password;

    /**
     * 类型 1：系统管理员，2：用户
     */
    @ApiModelProperty(value = "类型 1：系统管理员，2：用户", name = "type")
    private Integer type;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "userName")
    private String userName;

    /**
     * 状态 0：禁用，1：正常
     */
    @ApiModelProperty(value = "状态 0：禁用，1：正常", name = "state")
    private Integer state;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createUser")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Long createTime;


    @ApiModelProperty(value = "过期时间", name = "expireIn")
    private Long expireTime;
}
