package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@RestController
@RequestMapping("/sys/role")
@Api(value = "角色接口", tags = {"角色接口"})
public class SysRoleController extends BaseController {

    private final static String qxurl = "/sys/role";


    @Autowired
    private SysRoleService roleService;


    @PostMapping("/saveRole")
    @Permission(url = qxurl, type = PermissionType.ADD)
    @ApiOperation("添加角色")
    public Wrapper saveRole(@RequestParam("roleName") String roleName, @RequestParam("description") String description) {
        return roleService.saveRole(roleName, description, getLoginAuthDto());
    }

    @DeleteMapping("/deleteRole")
    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除角色")
    public Wrapper deleteRole(@RequestParam("id") Long id) {
        return roleService.deleteRole(id, getLoginAuthDto());
    }


    @PutMapping("/editRole")
    @Permission(url = qxurl, type = PermissionType.EDIT)
    @ApiOperation("修改角色")
    public Wrapper editRole(@RequestParam("id") Long id, @RequestParam("roleName") String roleName, @RequestParam("description") String description) {
        return roleService.editRole(id, roleName, description, getLoginAuthDto());
    }

    @GetMapping("/getRole")
    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("获取角色")
    public Wrapper getRole(@RequestParam("id") Long id) {
        return roleService.getRole(id, getLoginAuthDto());
    }

    @GetMapping("/listRole")
    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("获取角色列表")
    public Wrapper listRole() {
        return roleService.listRole(getLoginAuthDto());
    }

    @PutMapping("/setRole")
    @Permission(url = qxurl, type = PermissionType.EDIT)
    @ApiOperation("停用启用")
    public Wrapper setRole(@RequestParam("id") Long id,@ApiParam("1：停用 0：启用") @RequestParam("state") Integer state) {
        return roleService.setRole(id, state, getLoginAuthDto());
    }

}
