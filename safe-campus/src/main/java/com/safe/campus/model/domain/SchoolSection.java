package com.safe.campus.model.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 学校部门
 * </p>
 *
 * @author jobob
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolSection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    private Long id;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long tId;


    private Integer level;

    /**
     * 主校ID
     */
    private Long masterId;

    /**
     * PID
     */
    private Long pId;

    /**
     * 部门名称
     */
    private String sectionName;

    /**
     * 状态：1：停用 0：正常
     */
    private Integer state;

    /**
     * 删除：1：删除 0：未删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private Long createdUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdTime;


}
