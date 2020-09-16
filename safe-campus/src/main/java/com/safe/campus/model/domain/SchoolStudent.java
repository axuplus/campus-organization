package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author Joma
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SchoolStudent对象", description="学生表")
public class SchoolStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "masterId")
    private Long masterId;

    @ApiModelProperty(value = "学生姓名")
    private String sName;

    @ApiModelProperty(value = "学号")
    private String sNumber;

    @ApiModelProperty(value = "年级ID")
    private Long classId;

    @ApiModelProperty(value = "班级ID")
    private Long classInfoId;

    @ApiModelProperty(value = "性别1：男 0：女")
    private Integer sex;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "1：住校生 2：同校生")
    private Integer type;

    @ApiModelProperty(value = "入学年份")
    private String joinTime;

    @ApiModelProperty(value = "入学年份")
    private String endTime;

    @ApiModelProperty(value = "照片ID")
    private Long imgId;

    @ApiModelProperty(value = "是否删除1：已删除 0：未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建用户")
    private Long createdUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdTime;
}
