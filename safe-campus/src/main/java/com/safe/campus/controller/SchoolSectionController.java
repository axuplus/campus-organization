package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.model.dto.SchoolSectionDto;
import com.safe.campus.model.dto.SchoolSectionInfoDto;
import com.safe.campus.service.SchoolSectionService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/campus/school/section")
@Api(value = "部门管理", tags = {"部门管理"})
public class SchoolSectionController {

    private final static String qxurl = "/campus/school/section";


    @Autowired
    private SchoolSectionService sectionService;

    @GetMapping("/school")
    @ApiOperation("1：获取学校")
    public Wrapper<Map<Long, String>> getSchools() {
        return sectionService.getSchools();
    }


    @GetMapping("/tree")
    @ApiOperation("2：获取部门以及下面节点")
    public Wrapper getDetailsSchoolSection(@RequestParam("schoolId") Long id) {
        return sectionService.getDetailsSection(id);
    }


    @Permission(url = qxurl,type = PermissionType.ADD)
    @PostMapping("/save")
    @ApiOperation("添加部门信息")
    public Wrapper saveSchoolSection(@RequestBody SchoolSectionDto schoolSectionDto) {
        return sectionService.saveSchoolSection(schoolSectionDto);
    }

    @GetMapping("/charge")
    @ApiOperation("获取部门负责人")
    public Wrapper<Map<Long, String>> getCharge() {
        return sectionService.getCharge();
    }



    @PutMapping("/edit")
    @ApiOperation("修改部门信息")
    public Wrapper editSchoolSection(@RequestBody SchoolSectionInfoDto schoolSectionInfoDto) {
        return sectionService.editSchoolSection(schoolSectionInfoDto);
    }


    @GetMapping("/get")
    @ApiOperation("获取部门信息")
    public Wrapper getSchoolSection(@RequestParam("id") Long id) {
        return sectionService.getSchoolSection(id);
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除部门信息")
    public Wrapper deleteSchoolSection(@PathVariable("id") Long id) {
        return sectionService.deleteSchoolSection(id);
    }


    @PutMapping("/activate")
    @ApiOperation("停用/启用")
    public Wrapper activeSchoolSection(@RequestParam("id") Long id, @ApiParam("1:停用 0: 启用") @RequestParam("type") Integer type) {
        return sectionService.activeSchoolSection(id, type);
    }

    @GetMapping("/list")
    @ApiOperation("获取部门信息列表")
    public Wrapper listSchoolSection(@RequestParam("sectionId")Long sectionId) {
        return sectionService.listSchoolSection(sectionId);
    }


    @GetMapping("/search")
    @ApiOperation("搜索")
    public Wrapper searchSchoolSection(@RequestParam("name") String name) {
        return sectionService.searchSchoolSection(name);
    }
}
