package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuListVo {

    @ApiModelProperty(value = "模块名称")
    private String moduleName;
    private menuInfo menuInfo;

    @Data
    public static class menuInfo {
        @ApiModelProperty(value = "id")
        private Long id;
        @ApiModelProperty(value = "1：增 2：删 3：改 4：查 5：权限")
        private Integer type;
        @ApiModelProperty(value = "权限描述")
        private String description;
    }
}
