package com.safe.campus.controller;


import com.github.pagehelper.PageInfo;
import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.BuildingStudentDto;
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
    @ApiOperation("1：添加楼幢")
    @PostMapping("/save/buildingNo")
    public Wrapper saveBuilding(@RequestParam("masterId") Long masterId, @RequestParam("buildingName") String buildingName) {
        return buildingService.saveBuilding(buildingName, masterId, getLoginAuthDto());
    }

    @ApiOperation("2：添加楼层")
    @PostMapping("/save/buildingLevel")
    public Wrapper saveBuildingLevel(@RequestParam("buildingNoId") Long buildingNoId, @RequestParam("level") Integer level) {
        return buildingService.saveBuildingLevel(buildingNoId, level, getLoginAuthDto());
    }

    @ApiOperation("3：添加房间")
    @PostMapping("/save/buildingRoom")
    public Wrapper saveBuildingRoom(@RequestParam("buildingLevelId") Long buildingLevelId, @RequestParam("buildingRoom") Integer buildingRoom) {
        return buildingService.saveBuildingRoom(buildingLevelId, buildingRoom, getLoginAuthDto());
    }

    @ApiOperation("4：添加房间")
    @PostMapping("/save/buildingBed")
    public Wrapper saveBuildingBed(@RequestParam("buildingRoomId") Long buildingRoomId, @RequestParam("buildingBed") Integer buildingBed) {
        return buildingService.saveBuildingBed(buildingRoomId, buildingBed, getLoginAuthDto());
    }

    @ApiOperation("5：添加学生")
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


//    @ApiOperation("获取修改学生信息")
//    @GetMapping("/get/buildingStudent")
//    public Wrapper<BuildingRoomVo> getBuilding(@RequestParam("id") Long id) {
//        return buildingService.getBuilding(id);
//    }
//
//
//    @ApiOperation("确认修改学生信息")
//    @PutMapping("/edit/buildingStudent")
//    public Wrapper editBuilding(@ApiParam("buildingStudentId") @RequestParam("id") Long id, @RequestParam("studentId") Long studentId) {
//        return buildingService.editBuilding(id, studentId);
//    }


    @ApiOperation("获取楼幢信息以及层级")
    @GetMapping("/get/tree")
    public Wrapper<List<BuildingTreeVo>> getBuildingTree(@RequestParam("masterId") Long masterId) {
        return buildingService.getBuildingTree(masterId);
    }

    @Permission(url = qxurl, type = PermissionType.DEL)
    @ApiOperation("删除")
    @DeleteMapping("/delete/{type}/{id}")
    public Wrapper deleteBuilding(@ApiParam("1：楼幢ID 2：楼层ID 3：宿舍ID 4：删除宿舍学生 ") @PathVariable("type") Integer type, @PathVariable("id") Long id) {
        return buildingService.deleteBuilding(type, id);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("楼幢 管理员列表")
    @GetMapping("/managerList")
    public PageWrapper<List<BuildingManagerVo>> managerList(@ApiParam("楼幢ID ") @RequestParam("id") Long id, BaseQueryDto baseQueryDto) {
        return buildingService.managerList(id, baseQueryDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("楼层 学生列表")
    @GetMapping("/levelStudentList")
    public  PageWrapper<List<BuildingStudentListVo>> levelStudentList(@ApiParam("楼层ID ") @RequestParam("id") Long id, BaseQueryDto baseQueryDto) {
        return buildingService.levelStudentList(id, baseQueryDto);
    }

    @ApiOperation("宿舍 学生列表")
    @GetMapping("/roomStudentList")
    public  PageWrapper<List<BuildingStudentListVo>> roomStudentList(@ApiParam("宿舍ID ") @RequestParam("id") Long id, BaseQueryDto baseQueryDto) {
        return buildingService.roomStudentList(id, baseQueryDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @ApiOperation("搜索")
    @GetMapping("/search")
    public Wrapper searchList(@RequestParam("masterId") Long masterId,
                              @ApiParam("1:宿管页面 2:宿舍页面") @RequestParam("type") Integer type,
                              @RequestParam("context") String context) {
        return buildingService.searchList(masterId, type, context);
    }


    @ApiOperation("修改宿管老师下拉列表")
    @GetMapping("/get/buildingTeachers")
    public Wrapper<Map<Long, String>> getBuildingTeachers(@RequestParam("masterId")Long masterId) {
        return buildingService.getBuildingTeachers(masterId);
    }

    @ApiOperation("获取楼幢宿管老师")
    @GetMapping("/get/buildingTeacher")
    public Wrapper<BuildingTeacherVo> getBuildingTeacher(@ApiParam("楼层ID") @RequestParam("levelId") Long levelId) {
        return buildingService.getBuildingTeacher(levelId);
    }

    @Permission(url = qxurl,type = PermissionType.EDIT)
    @ApiOperation("确认修改宿管老师")
    @PutMapping("/set/buildingTeacher")
    public Wrapper setBuildingTeacher(@RequestParam("levelId") Long levelId, @RequestParam("teacherId") Long teacherId) {
        return buildingService.setBuildingTeacher(levelId, teacherId);
    }

    // 楼幢列表删除
    @Permission(url = qxurl,type = PermissionType.DEL)
    @ApiOperation("删除楼幢管理员")
    @DeleteMapping("/delete/{levelId}")
    public Wrapper deleteBuildingManger( @PathVariable("levelId") Long levelId) {
        return buildingService.deleteBuildingManger(levelId);
    }
}
