package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 学校年级表
 * </p>
 *
 * @author jobob
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    private Long id;

    /**
     * 总校ID
     */
    private Long masterId;

    /**
     * 年级 1：一年级 依次排列
     */
    private String className;

    /**
     * 创建用户
     */
    private Long createdUser;

    /**
     * 主任ID
     */
    private Long tId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdTime;

    /**
     * 是否删除 1：已删除 0：未删除
     */
    private Integer isDelete;

    /**
     * 是否停用
     */
    private Integer state;

}
