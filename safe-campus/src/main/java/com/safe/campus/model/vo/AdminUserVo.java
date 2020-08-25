package com.safe.campus.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AdminUserVo {

    private Long id;
    private String userName;
    private Integer state;
    private String parentName;
    private Date createTime;
}
