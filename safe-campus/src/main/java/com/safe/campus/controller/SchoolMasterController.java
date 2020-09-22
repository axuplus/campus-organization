package com.safe.campus.controller;


import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.SaveOrEditNodeDto;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.dto.SchoolMasterDto;
import com.safe.campus.model.dto.SchoolMaterConfDto;
import com.safe.campus.service.SchoolMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 学校总校区表 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-18
 */
@RestController
@RequestMapping("/school/master")
@Api(value = "账号管理", tags = {"账号管理"})
public class SchoolMasterController extends BaseController {


    @Autowired
    private SchoolMasterService masterService;


    @PostMapping("/save")
    @ApiOperation("添加学校")
    public Wrapper saveSchool(@RequestBody SchoolMasterDto schoolMasterDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.saveSchool(loginAuthDto, schoolMasterDto);
    }

    @GetMapping("/get")
    @ApiOperation("查看学校")
    public Wrapper getSchool(@RequestParam("id") Long id) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.getSchool(loginAuthDto, id);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑学校")
    public Wrapper editSchool(@RequestBody SchoolMasterDto schoolMasterDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.editSchool(loginAuthDto, schoolMasterDto);
    }


    @GetMapping("/list")
    @ApiOperation("学校列表")
    public Wrapper listSchool() {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listSchool(loginAuthDto);
    }

    @PostMapping("/saveOrEditNode")
    @ApiOperation("添加节点")
    public Wrapper saveOrEditNode(@RequestBody  SaveOrEditNodeDto saveOrEditNodeDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.saveOrEditNode(saveOrEditNodeDto);
    }

    @DeleteMapping("/deleteNode/{id}")
    @ApiOperation("删除节点")
    public Wrapper deleteNode(@PathVariable("id") Long rootId) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.deleteNode(loginAuthDto, rootId);
    }

    @GetMapping("/listNode")
    @ApiOperation("展示节点")
    public Wrapper listNode() {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listNode(loginAuthDto);
    }

    @GetMapping("/listSchoolConf")
    @ApiOperation("获取当前学校模块配置")
    public Wrapper listSchoolConf(@RequestParam("masterId") Long masterId) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listSchoolConf(loginAuthDto, masterId);
    }

    @GetMapping("/listConf")
    @ApiOperation("获取模块配置")
    public Wrapper listConf() {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listConf(loginAuthDto);
    }

    @PostMapping("/saveConf")
    @ApiOperation("保存模块配置")
    public Wrapper saveConf(@RequestBody SchoolMaterConfDto schoolMaterConfDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.saveConf(loginAuthDto, schoolMaterConfDto);
    }

    @GetMapping("/listProvince")
    @ApiOperation("省")
    public Wrapper listProvince() {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listProvince(loginAuthDto);
    }

    @GetMapping("/listCity")
    @ApiOperation("市")
    public Wrapper listCity(@RequestParam("provinceId") Long provinceId) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listCity(loginAuthDto, provinceId);
    }

    @GetMapping("/listArea")
    @ApiOperation("区")
    public Wrapper listArea(@RequestParam("cityId") Long cityId) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.listArea(loginAuthDto, cityId);
    }

    @PutMapping("/reset")
    @ApiOperation("重置密码")
    public Wrapper resetPassword(@RequestParam("account") String account) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return masterService.resetPassword(loginAuthDto, account);
    }


}
