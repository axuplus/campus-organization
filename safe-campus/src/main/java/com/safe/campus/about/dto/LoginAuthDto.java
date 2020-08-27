package com.safe.campus.about.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LoginAuthDto implements Serializable {

    private static final long serialVersionUID = -1137852221455042256L;

    private Integer type;
    //private Long masterId;
    private Long userId;
    private String userName;
    private String token;
    private Long expireTime;
    private Long createTime;
}
