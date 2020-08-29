package com.safe.campus.model.vo;

import com.safe.campus.model.domain.RouteConf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModuleConfVo {

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户类型 1:安校账号 2:学校账号 3:学校子用户账号")
    private Integer type;
    @ApiModelProperty(value = "学校ID 如果学校为1的话此id为空")
    private Long masterId;
    @ApiModelProperty(value = "模块配置")
    private List<RouteConf> confs;
}
