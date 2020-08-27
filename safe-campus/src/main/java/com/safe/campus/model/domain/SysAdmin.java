package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SysAdmin {

    /**
     * ID
     */
    private Long id;
    /**
     * 密码
     */
    private String password;

    /**
     * 名称
     */
    private String userName;

    /**
     * 状态 0：禁用，1：正常
     */
    private Integer state;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 类型
     */
    private Integer type;

    /**
     * key
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * masterId
     */
    private Long masterId;

    /**
     * tId
     */
    private Long tId;


}
