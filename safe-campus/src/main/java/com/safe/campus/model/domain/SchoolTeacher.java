package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 教职工信息
 * </p>
 *
 * @author Joma
 * @since 2020-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SchoolTeacher对象", description = "教职工信息")
public class SchoolTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

    private Long masterId;

    @ApiModelProperty(value = "教师姓名")
    private String tName;

    @ApiModelProperty(value = "教师工号")
    private String tNumber;

    @ApiModelProperty(value = "性别 1：男  0：女  -1：未知")
    private Integer sex;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "电话号码")
    private Long phone;

    @ApiModelProperty(value = "部门ID")
    private Long sectionId;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "入职年份")
    private String joinTime;

    @ApiModelProperty(value = "1：已离职 0：在职")
    private Integer state;

    @ApiModelProperty(value = "1：已删除 0：未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建用户")
    private Long createdUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "图片ID")
    private Long imgId;
}

