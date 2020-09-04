package com.safe.campus.controller;

import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.LoginTokenDto;
import com.safe.campus.model.vo.LoginTokenVo;
import com.safe.campus.model.vo.ModuleConfVo;
import com.safe.campus.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * token Controller
 */
@RestController
@RequestMapping(value = "/admin")
@Api(value = "token接口", tags = {"token接口"})
public class TokenController extends BaseController {


    @Autowired
    private TokenService tokenService;

    /**
     * 获取token
     */
    @PostMapping(value = "/token")
    @ApiOperation(value = "获取token", notes = "获取token")
    public Wrapper<LoginTokenVo> token(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        final LoginTokenVo result = tokenService.token(userName, password);
        return WrapMapper.ok(result);
    }

    /**
     * 退出系统
     */
    @PostMapping(value = "/exit")
    @ApiOperation(value = "退出系统", notes = "退出系统")
    public Wrapper exit() {
        final LoginAuthDto loginAuthDto = getLoginAuthDto();
        final Boolean flag = tokenService.exit(loginAuthDto.getUserId());
        if (flag) {
            return WrapMapper.ok("退出成功");
        }
        return WrapMapper.wrap(100002, "退出失败");
    }


    @GetMapping(value = "/conf")
    @ApiModelProperty(value = "获取当前模块配置 (登录成功后首先调这个接口)", notes = "获取当前模块配置")
    public Wrapper<ModuleConfVo> getModuleConf(@RequestParam("userId") Long userId) {
        return tokenService.getModuleConf(userId);
    }
}
