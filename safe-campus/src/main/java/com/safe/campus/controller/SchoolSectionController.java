package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.SchoolSectionDto;
import com.safe.campus.model.dto.SchoolSectionInfoDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.SchoolSectionService;
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
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/campus/school/section")
@Api(value = "部门管理", tags = {"部门管理"})
public class SchoolSectionController extends BaseController {

    private final static String qxurl = "/campus/school/section";


    @Autowired
    private SchoolSectionService sectionService;

    @GetMapping("/school")
    @ApiOperation("1：获取学校")
    public Wrapper<Map<Long, String>> getSchools(@RequestParam("masterId") Long masterId) {
        return sectionService.getSchools(masterId, getLoginAuthDto());
    }


    @GetMapping("/tree")
    @ApiOperation("2：获取部门以及下面节点")
    public Wrapper<List<SectionTreeVo>> getDetailsSchoolSection(@RequestParam("masterId") Long id) {
        return sectionService.getDetailsSection(id);
    }


    @Permission(url = qxurl, type = PermissionType.ADD)
    @PostMapping("/save")
    @ApiOperation("添加部门信息")
    public Wrapper saveSchoolSection(@RequestBody SchoolSectionDto schoolSectionDto) {
        return sectionService.saveSchoolSection(schoolSectionDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/get")
    @ApiOperation("获取部门信息")
    public Wrapper<SchoolSectionVo> getSchoolSection(@RequestParam("id") Long id) {
        return sectionService.getSchoolSection(id);
    }

    @GetMapping("/superior")
    @ApiOperation("上级列表")
    public Wrapper<List<SectionVo>> getSuperior(@ApiParam("学校ID")@RequestParam(value = "masterId") Long masterId,@RequestParam("sectionId")Long sectionId) {
        return sectionService.getSuperior(masterId,sectionId);
    }

    @GetMapping("/charge")
    @ApiOperation("部门负责人列表")
    public Wrapper<List<SectionTeachersVo>> getCharge(@RequestParam("masterId") Long masterId) {
        return sectionService.getCharge(masterId);
    }


    @Permission(url = qxurl, type = PermissionType.EDIT)
    @PostMapping("/edit")
    @ApiOperation("修改部门信息")
    public Wrapper editSchoolSection(@RequestBody SchoolSectionInfoDto schoolSectionInfoDto) {
        return sectionService.editSchoolSection(schoolSectionInfoDto);
    }


    @Permission(url = qxurl, type = PermissionType.DEL)
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除部门信息")
    public Wrapper deleteSchoolSection(@PathVariable("id") Long id) {
        return sectionService.deleteSchoolSection(id);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/list")
    @ApiOperation("获取部门信息列表")
    public PageWrapper<List<SchoolSectionVo>> listSchoolSection(@RequestParam("masterId") Long masterId,
                                                                @ApiParam("1:全部0:部门列表") @RequestParam("type") Integer type,
                                                                @ApiParam("如果是学校节点不用传部门ID") @RequestParam(value = "sectionId", required = false) Long sectionId,
                                                                BaseQueryDto baseQueryDto) {
        return sectionService.listSchoolSection(masterId, type, sectionId, baseQueryDto);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/search")
    @ApiOperation("搜索")
    public PageWrapper<List<SchoolSectionVo>> searchSchoolSection(@RequestParam("masterId") Long masterId, @RequestParam("context") String name, BaseQueryDto baseQueryDto) {
        return sectionService.searchSchoolSection(masterId, name, baseQueryDto);
    }
}
