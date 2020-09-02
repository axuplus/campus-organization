package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AdminUserVo {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "姓名")
    private String userName;
    @ApiModelProperty(value = "状态")
    private Integer state;
    @ApiModelProperty(value = "创建人")
    private String parentName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
