package com.safe.campus.controller;


import com.github.pagehelper.PageInfo;
import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.BuildingStudentDto;
import com.safe.campus.model.dto.SaveBuildingInfoDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.BuildingService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼幢表 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/campus/school/building")
@Api(value = "楼幢管理", tags = {"楼幢管理"})
public class BuildingController extends BaseController {

    private final static String qxurl = "/campus/school/building";


    @Autowired
    private BuildingService buildingService;


    @Permission(url = qxurl, type = PermissionType.ADD)
    @ApiOperation("1: 添加楼幢 /2: 楼层 3: /房间 4: /床位")
    @PostMapping("/save")
    public Wrapper saveBuilding(@RequestBody SaveBuildingInfoDto saveBuildingInfoDto) {
        return buildingService.saveBuilding(saveBuildingInfoDto, getLoginAuthDto());
    }


    @ApiOperation("2：添加学生")
    @PostMapping("/save/buildingStudent")
    public Wrapper saveBuildingStudent(@RequestBody BuildingStudentDto buildingStudentDto) {
        return buildingService.saveBuildingStudent(buildingStudentDto);
    }

    @ApiOperation("1:选择年级")
    @GetMapping("/get/class")
    public Wrapper<List<BuildingClassVo>> getAllClass(@RequestParam("masterId") Long masterId) {
        return buildingService.getAllClass(masterId);
    }


    @ApiOperation("2:选择班级")
    @GetMapping("/get/classInfo")
    public Wrapper<List<BuildingClassVo>> getAllClassInfo(@RequestParam("classId") Long classId) {
        return buildingService.getAllClassInfo(classId);
    }


    @ApiOperation("3:选择学生")
    @GetMapping("/get/student")
    public Wrapper<List<BuildingStudentVo>> getAllStudent(@RequestParam("classInfoId") Long classInfoId) {
        return buildingService.getAllStudent(classInfoId);
    }


    @ApiOperation("获取楼幢信息以及层级")
    @GetMapping("/tree")
    public Wrapper<List<BuildingTreeVo>> getBuildingTree(@RequestParam("masterId") Long masterId) {
        return buildingService.getBuildingTree(masterId);
    }

    @ApiOperation("修改树结构名称")
    @GetMapping("/edit")
    public Wrapper editBuildingTree(@ApiParam("1 楼幢 2 楼层 3 宿舍 4 床位") @RequestParam("type") Integer type,
                                    @RequestParam("id") Long id,
                                    @RequestParam("name") String name) {
        return buildingService.editBuildingTree(type, id, name);
    }


    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除->树")
    @DeleteMapping("/delete/{type}/{id}")
    public Wrapper deleteBuilding(@ApiParam("1：楼幢ID 2：楼层ID 3：宿舍ID 4：床位id ") @PathVariable("type") Integer type, @PathVariable("id") Long id) {
        return buildingService.deleteBuilding(type, id);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("楼幢 管理员列表")
    @GetMapping("/managerList")
    public PageWrapper<List<BuildingManagerVo>> managerList(
            @ApiParam("楼幢ID ") @RequestParam(value = "id", required = false) Long id,
            @RequestParam("masterId") Long materId,
            BaseQueryDto baseQueryDto) {
        return buildingService.managerList(id, materId, baseQueryDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("楼层/宿舍 学生列表")
    @GetMapping("/studentList")
    public PageWrapper<List<BuildingStudentListVo>> studentList(@ApiParam("1:全部2:楼层3:宿舍") @RequestParam("type") Integer type,
                                                                @ApiParam("楼层/宿舍ID ") @RequestParam("id") Long id,
                                                                @RequestParam("masterId") Long masterId,
                                                                BaseQueryDto baseQueryDto) {
        return buildingService.studentList(type, id, masterId, baseQueryDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("搜索")
    @GetMapping("/search")
    public Wrapper searchList(@RequestParam("masterId") Long masterId,
                              @ApiParam("1:宿管页面 2:宿舍页面") @RequestParam("type") Integer type,
                              @RequestParam("context") String context) {
        return buildingService.searchList(masterId, type, context);
    }


    @ApiOperation("配置管理人下拉列表")
    @GetMapping("/get/buildingTeachers")
    public Wrapper<List<SchoolClassTeachersVo>> listTeachers(@RequestParam("masterId") Long masterId) {
        return buildingService.getBuildingTeachers(masterId);
    }


    @ApiOperation("添加管理人（获取信息）")
    @GetMapping("/get/buildingInfo")
    public Wrapper<BuildingTeacherVo> getBuildingTeacher(@ApiParam("楼层ID") @RequestParam("levelId") Long levelId) {
        return buildingService.getBuildingTeacher(levelId);
    }

    @Permission(url = qxurl, type = PermissionType.EDIT)
    @ApiOperation("确认修改管理人")
    @GetMapping("/set/buildingTeacher")
    public Wrapper setBuildingTeacher(@RequestParam("levelId") Long levelId, @RequestParam("teacherId") Long teacherId) {
        return buildingService.setBuildingTeacher(levelId, teacherId);
    }

    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除->楼幢列表")
    @DeleteMapping("/delete/building/{levelId}")
    public Wrapper deleteBuildingManger(@PathVariable("levelId") Long levelId) {
        return buildingService.deleteBuildingManger(levelId);
    }

    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除->学生列表")
    @DeleteMapping("/delete/student/{sId}")
    public Wrapper deleteBuildingStudent(@PathVariable("sId") Long sId) {
        return buildingService.deleteBuildingStudent(sId);
    }
}
