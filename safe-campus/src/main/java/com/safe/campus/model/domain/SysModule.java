package com.safe.campus.model.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysMoudle对象", description = "权限菜单模块名称")
public class SysModule {

    private Long id;
    private String moduleName;
    private String belongs;
    private String owned;
}
