package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.service.SysAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@RestController
@RequestMapping("/sys/admin")
@Api(value = "admin接口", tags = {"admin接口"})
public class SysAdminController extends BaseController {

    private final static String qxurl = "/sys/admin";


    @Autowired
    private SysAdminService adminService;

    @PostMapping("/saveUser")
    @Permission(url = qxurl, type = PermissionType.ADD)
    @ApiOperation("添加用户")
    public Wrapper saveAdminUser(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        return adminService.saveAdminUser(userName, password, getLoginAuthDto());
    }

    @DeleteMapping("/delUser")
    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除用户")
    public Wrapper deleteAdminUser(@RequestParam("id") Long userId) {
        return adminService.deleteAdminUser(userId, getLoginAuthDto());
    }

    @PutMapping("/editUser")
    @Permission(url = qxurl, type = PermissionType.EDIT)
    @ApiOperation("编辑用户")
    public Wrapper editAdminUser(@RequestParam("id") Long userId, @RequestParam("userName") String userName, @RequestParam("password") String password) {
        return adminService.editAdminUser(userId, userName, password, getLoginAuthDto());
    }

    @GetMapping("/getUser")
    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("查看用户")
    public Wrapper getAdminUser(@RequestParam("id") Long userId) {
        return adminService.getAdminUser(userId, getLoginAuthDto());
    }


    @GetMapping("/listUser")
    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("用户列表")
    public Wrapper listAdminUser() {
        return adminService.listAdminUser(getLoginAuthDto());
    }

}
