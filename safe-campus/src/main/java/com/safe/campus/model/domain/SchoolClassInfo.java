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
 * 学校班级表
 * </p>
 *
 * @author jobob
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    private Long id;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 学校班级
     */
    private String classInfoName;

    /**
     * 班主任ID
     */
    private Long  tId;

    /**
     * 状态 1：停用  0：正常
     */
    private Integer state;

    /**
     * 删除 1：已删除 0：未删除
     */
    private Integer isDelete;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


}
