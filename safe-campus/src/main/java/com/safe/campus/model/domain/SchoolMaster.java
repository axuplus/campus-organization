package com.safe.campus.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学校总校区表
 * </p>
 *
 * @author jobob
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "节点id")
    private Long rootId;

    @ApiModelProperty(value = "校区名称")
    private String areaName;

    @ApiModelProperty(value = "省市区")
    private String address;

    @ApiModelProperty(value = "校区地址")
    private String areaAddress;

    @ApiModelProperty(value = "服务时间")
    private String serviceTime;

    @ApiModelProperty(value = "当前状态1：已禁用 0：未禁用")
    private Integer state;

    @ApiModelProperty(value = "学校logo")
    private String logo;

    @ApiModelProperty(value = "校园实景图")
    private String realPicture;

    @ApiModelProperty(value = "1：已删除 0：未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建人")
    private Long createdUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;


    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "轮播图")
    private List<Long> imgs;
}
