package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.dto.SchoolClassInfoDto;
import com.safe.campus.model.vo.NodeTreeVo;
import com.safe.campus.model.vo.SchoolClassEditVo;
import com.safe.campus.model.vo.SchoolClassSearchVo;
import com.safe.campus.model.vo.SchoolClassTeachersVo;
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
    @PostMapping("/editClass")
    @ApiOperation("编辑年级")
    public Wrapper editClass(@RequestBody SchoolClassDto schoolClassDto) {
        return schoolClassService.editClass(schoolClassDto, getLoginAuthDto());
    }

    @PostMapping("/editClassInfo")
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



    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/search")
    @ApiOperation("搜索")
    public Wrapper<List<SchoolClassSearchVo>> searchSchoolClass(@RequestParam("masterId") Long masterId, @RequestParam("context") String name) {
        return schoolClassService.searchSchoolClass(masterId, name);
    }

    @GetMapping("/tree")
    @ApiOperation("左边节点树")
    public Wrapper<List<NodeTreeVo>> nodeTreeSchoolClass(@RequestParam("masterId")Long masterId) {
        return schoolClassService.nodeTreeSchoolClass(masterId);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/list")
    @ApiOperation("右边年级列表列表(点击年级/班级)")
    public Wrapper<List<SchoolClassSearchVo>> listClass(@RequestParam("masterId") Long masterId,
                                                            @RequestParam("id") Long id,
                                                            @ApiParam("1年级 2班级 3全部") @RequestParam("type") Integer type
                                                        ) {
        return schoolClassService.listClass(masterId, id, type);
    }


    @GetMapping("/list/teachers")
    @ApiOperation(value = "管理人列表", notes = "管理人列表")
    public Wrapper<List<SchoolClassTeachersVo>> listTeachers(@RequestParam("masterId")Long masterId) {
        return schoolClassService.listTeachers(masterId);
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取", notes = "修改获取信息")
    public Wrapper<SchoolClassEditVo> getInfo(@ApiParam("1:年级 2:班级") @RequestParam("type") Integer type, @RequestParam("id") Long id) {
        return schoolClassService.getInfo(type, id);
    }
}
