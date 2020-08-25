package com.safe.campus.model.dto;

import com.safe.campus.about.dto.LoginAuthDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserTokenDto implements Serializable {

    private static final long serialVersionUID = -1137852221455042256L;
    private String userName;
    private Long userId;
    private String token;
    private Integer type;
    private Long masterId;
    private Long expireTime;
    private Long createTime;
}
