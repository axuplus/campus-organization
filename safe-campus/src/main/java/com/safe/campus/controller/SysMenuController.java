package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SysModule;
import com.safe.campus.model.dto.MenuDto;
import com.safe.campus.model.vo.MenuListVo;
import com.safe.campus.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限菜单 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@RestController
@RequestMapping("/sys/menu")
@Api(value = "权限管理", tags = {"权限管理"})
public class SysMenuController extends BaseController {

    private final static String qxurl = "/sys/menu";

    @Autowired
    private SysMenuService menuService;


    @GetMapping("/listMenu")
    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("权限列表")
    public Wrapper<MenuListVo> listMenu(@RequestParam("roleId")Long roleId) {
        return menuService.listMenu(roleId);
    }

    @PostMapping("/setMenu")
    @Permission(url = qxurl, type = PermissionType.ADD)
    @ApiOperation("设置权限")
    public Wrapper setRoleMenu(@RequestBody MenuDto menuDto) {
        return menuService.setRoleMenu(menuDto.getRoleId(), menuDto.getMenuIds());
    }
}
