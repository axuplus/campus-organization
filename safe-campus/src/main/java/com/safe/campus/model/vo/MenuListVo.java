package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "MenuListVo", description = "权限菜单")
public class MenuListVo {

    private String name;
    private RoleIfo roleIfo;
    @Data
    public static class RoleIfo{
        private String name;
        private List<ModuleInfo> moduleInfo;
        @Data
        public static class ModuleInfo {
            @ApiModelProperty(value = "模块名称")
            private String moduleName;
            private List<MenuInfo> menuInfo;
            @Data
            public static class MenuInfo {
                @ApiModelProperty(value = "id")
                private Long id;
                @ApiModelProperty(value = "checked")
                private Boolean checked;
                @ApiModelProperty(value = "1：增 2：删 3：改 4：查 5：权限 6:停用启用")
                private Integer type;
                @ApiModelProperty(value = "权限描述")
                private String description;
            }
        }
    }
}
