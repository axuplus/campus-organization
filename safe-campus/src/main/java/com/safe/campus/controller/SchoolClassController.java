package com.safe.campus.controller;


import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.dto.SchoolClassInfoDto;
import com.safe.campus.model.vo.SchoolClassEditVo;
import com.safe.campus.model.vo.SchoolClassInfoVo;
import com.safe.campus.model.vo.SchoolClassVo;
import com.safe.campus.service.SchoolClassService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SchoolClassController extends BaseController {

    @Autowired
    private SchoolClassService schoolClassService;


    @PostMapping("/saveOrEdit")
    @ApiOperation("添加年级")
    public Wrapper saveSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return schoolClassService.saveSchoolClass(schoolClassDto);
    }

    @PostMapping("/info/saveOrEdit")
    @ApiOperation("添加班级")
    public Wrapper saveSchoolClassInfo(@RequestBody SchoolClassInfoDto schoolClassInfoDto) {
        return schoolClassService.saveSchoolClassInfo(schoolClassInfoDto);
    }

    @DeleteMapping("/delete/{type}/{id}")
    @ApiOperation("删除年级/班级")
    public Wrapper deleteSchoolClass(@ApiParam("1：删除年级 2：删除班级") @PathVariable("type") Integer type, @PathVariable("id") Long id) {
        return schoolClassService.deleteSchoolClass(type, id);
    }

    @PutMapping("/operation")
    @ApiOperation("年级->停用/启用")
    public Wrapper operationSchoolClassInfo(@ApiParam("1：停用 0：启用") @RequestParam("state") Integer state, @RequestParam("id") Long id) {
        return schoolClassService.operationSchoolClassInfo(state, id);
    }

    @PutMapping("/info/operation")
    @ApiOperation("班级->停用/启用")
    public Wrapper operationSchoolClass(@ApiParam("1：停用 0：启用") @RequestParam("state") Integer state, @RequestParam("id") Long id) {
        return schoolClassService.operationSchoolClass(state, id);
    }

    @GetMapping("/search")
    @ApiOperation("搜索")
    public Wrapper searchSchoolClass(@RequestParam("context") String name) {
        return schoolClassService.searchSchoolClass(name);
    }

    @GetMapping("/tree")
    @ApiOperation("左边节点树")
    public Wrapper nodeTreeSchoolClass() {
        return schoolClassService.nodeTreeSchoolClass();
    }

    @GetMapping("/list/class")
    @ApiOperation("右边年级列表列表(点击年级)")
    public Wrapper<List<SchoolClassVo>> listClass(@RequestParam("masterId") Long masterId) {
        return schoolClassService.listClass(masterId);
    }

    @GetMapping("/list/classInfo")
    @ApiOperation("右边班级列表列表(点击校区)")
    public Wrapper<List<SchoolClassInfoVo>> listClassInfo(@RequestParam("classId") Long id) {
        return schoolClassService.nodeTreeInfoSchoolClass(id);
    }

    @GetMapping("/list/teachers")
    @ApiOperation(value = "年级主任/班主任", notes = "map")
    public Wrapper<Map<Long, String>> listTeachers(@ApiParam("1:年级主任列表 2:班主任列表") Integer type) {
        return schoolClassService.listTeachers(type);
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取", notes = "修改获取信息")
    public Wrapper<SchoolClassEditVo> getInfo(@ApiParam("1:年级 2:班级") Integer type, @RequestParam("id") Long id) {
        return schoolClassService.getInfo(type, id);
    }


}
