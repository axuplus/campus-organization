package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.vo.SysRoleVo;
import com.safe.campus.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.codehaus.jackson.map.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/saveRole")
    @Permission(url = qxurl, type = PermissionType.ADD)
    @ApiOperation("添加角色")
    public Wrapper saveRole(@RequestParam("masterId")Long masterId,@RequestParam("roleName") String roleName, @RequestParam("description") String description) {
        return roleService.saveRole(masterId,roleName, description, getLoginAuthDto());
    }

    @DeleteMapping("/deleteRole/{id}")
    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除角色")
    public Wrapper deleteRole(@PathVariable("id") Long id) {
        return roleService.deleteRole(id, getLoginAuthDto());
    }


    @GetMapping("/editRole")
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
    public PageWrapper<List<SysRoleVo>> listRole(@RequestParam("masterId")Long masterId, BaseQueryDto baseQueryDto) {
        return roleService.listRole(masterId,baseQueryDto,getLoginAuthDto());
    }

    @GetMapping("/setRole")
    @Permission(url = qxurl, type = PermissionType.EDIT)
    @ApiOperation("停用启用")
    public Wrapper setRole(@RequestParam("id") Long id,@ApiParam("-1：停用 1：启用") @RequestParam("state") Integer state) {
        return roleService.setRole(id, state, getLoginAuthDto());
    }


}
