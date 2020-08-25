package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginTokenVo implements java.io.Serializable {

    @ApiModelProperty(value = "用户ID", name = "userId")
    private Long userId;

    @ApiModelProperty(value = "用户名称", name = "userName")
    private String userName;

    @ApiModelProperty(value = "用户凭证", name = "token")
    private String token;

    @ApiModelProperty(value = "类型：1：安校admin 2：学校账号 3：学校子账号", name = "type")
    private Integer type;

    @ApiModelProperty(value = "如果是type为2的话就是学校账号，这里是学校的ID", name = "masterId")
    private Long masterId;

    @ApiModelProperty(value = "过期时间", name = "expireIn")
    private Long expireIn;

}
