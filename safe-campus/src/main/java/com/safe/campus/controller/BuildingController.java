package com.safe.campus.controller;


import com.safe.campus.model.vo.*;
import com.safe.campus.service.BuildingService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class BuildingController {

    @Autowired
    private BuildingService buildingService;


    @ApiOperation("1：添加楼幢")
    @PostMapping("/save/buildingNo")
    public Wrapper saveBuilding(@RequestParam("buildingName") String buildingName) {
        return buildingService.saveBuilding(buildingName);
    }

    @ApiOperation("2：添加楼层")
    @PostMapping("/save/buildingLevel")
    public Wrapper saveBuildingLevel(@RequestParam("buildingNoId") Long buildingNoId, @RequestParam("level") Integer level) {
        return buildingService.saveBuildingLevel(buildingNoId, level);
    }

    @ApiOperation("3：添加房间")
    @PostMapping("/save/buildingRoom")
    public Wrapper saveBuildingRoom(@RequestParam("buildingLevelId") Long buildingLevelId, @RequestParam("buildingRoom") Integer buildingRoom) {
        return buildingService.saveBuildingRoom(buildingLevelId, buildingRoom);
    }

    @ApiOperation("4：添加房间")
    @PostMapping("/save/buildingBed")
    public Wrapper saveBuildingBed(@RequestParam("buildingRoomId") Long buildingRoomId, @RequestParam("buildingBed") Integer buildingBed) {
        return buildingService.saveBuildingBed(buildingRoomId, buildingBed);
    }

    @ApiOperation("5：添加学生")
    @PostMapping("/save/buildingStudent")
    public Wrapper saveBuildingStudent(
            @RequestParam("buildingRoomId") Long buildingRoomId,
            @RequestParam("studentId") Long studentId,
            @RequestParam("bedId") Long bedId) {
        return buildingService.saveBuildingStudent(buildingRoomId, studentId, bedId);
    }

    @ApiOperation("1:选择年级")
    @GetMapping("/get/class")
    public Wrapper<List<BuildingClassVo>> getAllClass() {
        return buildingService.getAllClass();
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


    @ApiOperation("获取修改学生信息")
    @GetMapping("/get/buildingStudent")
    public Wrapper<BuildingRoomVo> getBuilding(@RequestParam("id") Long id) {
        return buildingService.getBuilding(id);
    }


    @ApiOperation("确认修改学生信息")
    @PutMapping("/edit/buildingStudent")
    public Wrapper editBuilding(@ApiParam("buildingStudentId") @RequestParam("id") Long id, @RequestParam("studentId") Long studentId) {
        return buildingService.editBuilding(id, studentId);
    }


    @ApiOperation("获取楼幢信息以及层级")
    @GetMapping("/get/tree")
    public Wrapper<List<BuildingTreeVo>> getBuildingTree() {
        return buildingService.getBuildingTree();
    }


    @ApiOperation("删除")
    @DeleteMapping("/delete/{type}/{id}")
    public Wrapper deleteBuilding(@ApiParam("1：楼幢ID 2：楼层ID 3：宿舍ID 4：删除宿舍学生 ") @PathVariable("type") Integer type, @PathVariable("id") Long id) {
        return buildingService.deleteBuilding(type, id);
    }


    @ApiOperation("楼幢 管理员列表")
    @GetMapping("/managerList")
    public Wrapper<List<BuildingManagerVo>> managerList(@ApiParam("楼幢ID ") @RequestParam("id") Long id) {
        return buildingService.managerList(id);
    }

    // 没有楼层列表

    @ApiOperation("宿舍列表")
    @GetMapping("/studentList")
    public Wrapper<List<BuildingStudentListVo>> roomList(@ApiParam("宿舍ID ") @RequestParam("id") Long id) {
        return buildingService.studentList(id);
    }

    @ApiOperation("搜索")
    @GetMapping("/search")
    public Wrapper<List<BuildingStudentListVo>> searchList(@RequestParam("context") String context) {
        return buildingService.searchList(context);
    }


    @ApiOperation("获取楼幢宿管老师列表")
    @GetMapping("/get/buildingTeachers")
    public Wrapper<Map<Long, String>> getBuildingTeachers() {
        return buildingService.getBuildingTeachers();
    }

    @ApiOperation("获取楼幢宿管老师")
    @GetMapping("/get/buildingTeacher")
    public Wrapper<BuildingTeacherVo> getBuildingTeacher(@ApiParam("楼幢ID") @RequestParam("id") Long buildingNoId) {
        return buildingService.getBuildingTeacher(buildingNoId);
    }

    @ApiOperation("确认修改宿管老师")
    @GetMapping("/set/buildingTeacher")
    public Wrapper setBuildingTeacher(@ApiParam("楼幢ID") @RequestParam("id") Long buildingNoId,@RequestParam("levels")List<Long>levels,@RequestParam("teacherId")Long teacherId) {
        return buildingService.setBuildingTeacher(buildingNoId,levels,teacherId);
    }

    @ApiOperation("删除楼幢管理员")
    @DeleteMapping("/delete/{id}/{levelId}")
    public Wrapper deleteBuildingManger(@ApiParam("楼幢ID") @PathVariable("id") Long id,@PathVariable("levelId")Long levelId) {
        return buildingService.deleteBuildingManger(id,levelId);
    }
}
