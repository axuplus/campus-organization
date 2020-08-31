package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.dto.SchoolClassInfoDto;
import com.safe.campus.model.vo.SchoolClassEditVo;
import com.safe.campus.model.vo.SchoolClassSearchVo;
import com.safe.campus.service.SchoolClassService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-30
 */
@RestController
@RequestMapping("/campus/school/class")
@Api(value = "班级管理", tags = {"班级管理"})
@Transactional(rollbackFor = Exception.class)
public class SchoolClassController extends BaseController {

    private final static String qxurl = "/campus/school/class";


    @Autowired
    private SchoolClassService schoolClassService;


    @Permission(url = qxurl, type = PermissionType.ADD)
    @PostMapping("/saveClass")
    @ApiOperation("添加年级")
    public Wrapper saveSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        return schoolClassService.saveSchoolClass(schoolClassDto, getLoginAuthDto());
    }

    @PostMapping("/saveClassInfo")
    @ApiOperation("添加班级")
    public Wrapper saveSchoolClassInfo(@RequestBody SchoolClassInfoDto schoolClassInfoDto) {
        return schoolClassService.saveSchoolClassInfo(schoolClassInfoDto, getLoginAuthDto());
    }

    @Permission(url = qxurl, type = PermissionType.EDIT)
    @PutMapping("/editClass")
    @ApiOperation("编辑年级")
    public Wrapper editClass(@RequestBody SchoolClassDto schoolClassDto) {
        return schoolClassService.editClass(schoolClassDto, getLoginAuthDto());
    }

    @PutMapping("/editClassInfo")
    @ApiOperation("编辑年级")
    public Wrapper editClassInfo(@RequestBody SchoolClassInfoDto schoolClassInfoDto) {
        return schoolClassService.editClassInfo(schoolClassInfoDto, getLoginAuthDto());
    }


    @Permission(url = qxurl, type = PermissionType.DEL)
    @DeleteMapping("/delete/{type}/{id}")
    @ApiOperation("删除年级/班级")
    public Wrapper deleteSchoolClass(@ApiParam("1：删除年级 2：删除班级") @PathVariable("type") Integer type, @PathVariable("id") Long id) {
        return schoolClassService.deleteSchoolClass(type, id);
    }

    @Permission(url = qxurl, type = PermissionType.ACTIVE)
    @PutMapping("/operation")
    @ApiOperation("年级->停用/启用")
    public Wrapper operation(@ApiParam("1:年级2:班级") @RequestParam("type") Integer type,
                             @ApiParam("1：停用 0：启用") @RequestParam("state") Integer state,
                             @RequestParam("id") Long id) {
        return schoolClassService.operation(type, state, id);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/search")
    @ApiOperation("搜索")
    public Wrapper searchSchoolClass(@RequestParam("masterId") Long masterId, @RequestParam("context") String name) {
        return schoolClassService.searchSchoolClass(masterId, name);
    }

    @GetMapping("/tree")
    @ApiOperation("左边节点树")
    public Wrapper nodeTreeSchoolClass(@RequestParam("masterId")Long masterId) {
        return schoolClassService.nodeTreeSchoolClass(masterId);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/list/class")
    @ApiOperation("右边年级列表列表(点击年级/班级)")
    public PageWrapper<List<SchoolClassSearchVo>> listClass(@RequestParam("masterId") Long masterId,
                                                            @RequestParam("id") Long id,
                                                            @ApiParam("1年级 2班级") @RequestParam("type") Integer type,
                                                            BaseQueryDto baseQueryDto) {
        return schoolClassService.listClass(masterId, id, type,baseQueryDto);
    }


    @GetMapping("/list/teachers")
    @ApiOperation(value = "年级主任/班主任", notes = "map")
    public Wrapper<Map<Long, String>> listTeachers(@RequestParam("masterId")Long masterId) {
        return schoolClassService.listTeachers(masterId);
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取", notes = "修改获取信息")
    public Wrapper<SchoolClassEditVo> getInfo(@ApiParam("1:年级 2:班级") @RequestParam("type") Integer type, @RequestParam("id") Long id) {
        return schoolClassService.getInfo(type, id);
    }
}
