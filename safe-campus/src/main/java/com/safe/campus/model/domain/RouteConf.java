package com.safe.campus.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 配置模块
 * </p>
 *
 * @author Joma
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RouteConf对象", description="配置模块")
public class RouteConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "子模块名称")
    private String subName;

    @ApiModelProperty(value = "路由")
    private String routeUrl;


}
