package com.safe.campus.controller;


import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.STDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.dto.TeacherByPhoneDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.ToOthersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/others")
@Api(value = "其他接口", tags = {"其他接口"})
public class ToOthersController {


    @Autowired
    private ToOthersService toOthersService;

    @GetMapping("/masters")
    @ApiOperation(value = "所有学校")
    public Wrapper<Map<Long, String>> getAllMasters() {
        return toOthersService.getAllMasters();
    }

    @GetMapping("/classes")
    @ApiOperation("查询所有年级")
    public Wrapper<Map<Long, String>> getAllClassesByMasterId(@RequestParam("masterId") Long masterId) {
        return toOthersService.getAllClassesByMasterId(masterId);
    }

    @GetMapping("/getIdNumberByUserId")
    @ApiOperation(value = "获取教职工身份证或学生身份证")
    public OthersDto getIdNumberByUserId(@RequestParam("type") String type, @RequestParam("userId") Long userId) {
        return toOthersService.getIdNumberByUserId(type, userId);
    }

    @PostMapping("/selectStudentList")
    @ApiOperation(value = "查询学生")
    public List<SelectStudentListDto> selectStudentList(@RequestBody Map map) {
        return toOthersService.selectStudentList(map);
    }

    @GetMapping("/getTeacherRoles")
    @ApiOperation(value = "获取教师角色")
    public String getTeacherRoles(@RequestParam("teacherId") Long teacherId) {
        return toOthersService.getTeacherRoles(teacherId);
    }


    @GetMapping("/getStudentTeacherByIdNumber")
    @ApiOperation(value = "获取学生班主任")
    public Map getStudentTeacherByIdNumber(@RequestParam("idNumber") String idNumber) {
        return toOthersService.getStudentTeacherByIdNumber(idNumber);
    }

    @GetMapping("/getStudentTeacherByStudentId")
    @ApiOperation(value = "获取学生班主任")
    public Map getStudentTeacherByStudentId(@RequestParam("studentId") Long studentId) {
        return toOthersService.getStudentTeacherByStudentId(studentId);
    }

    @GetMapping("/getTeacherForMiniApp")
    @ApiOperation(value = "获取老师信息")
    public OthersTeacherVo getTeacherForMiniApp(@RequestParam("idNumber") String idNumber) {
        return toOthersService.getTeacherForMiniApp(idNumber);
    }

    @GetMapping("/getStudentByIdNumber")
    @ApiOperation(value = "获取学生信息")
    public OthersStudentVo getStudentByIdNumber(@RequestParam("idNumber") String idNumber) {
        return toOthersService.getStudentByIdNumber(idNumber);
    }


    @GetMapping("/getFaceImgInfo")
    @ApiOperation(value = "人脸库人员信息")
    public Wrapper<FaceImgInfoVo> getFaceImgInfo(@ApiParam("S T") @RequestParam("type") String type, @RequestParam("id") Long id) {
        return toOthersService.getFaceImgInfo(type, id);
    }

    @GetMapping("/getTeacherByPhone")
    @ApiOperation(value = "获取学校id跟老师id")
    public TeacherByPhoneDto getTeacherByPhone(@RequestParam("teacherName") String teacherName, @RequestParam("phone") String phone) {
        return toOthersService.getTeacherByPhone(teacherName, phone);
    }

    @GetMapping("/getStudentAndTeacherInfo")
    @ApiOperation(value = "查询老师学生by id")
    public STDto getStudentAndTeacherInfo(@ApiParam("1老师 0学生") @RequestParam("type") Integer type, @RequestParam("id") Long id) {
        return toOthersService.getStudentAndTeacherInfo(type, id);
    }


    @GetMapping("/selectBuildingList")
    @ApiOperation(value = "获取楼幢或楼层或宿舍下拉列表")
    public Wrapper<Map> getBuildingInfoList(@ApiParam("学校id(必传)") @RequestParam(value = "masterId") Long masterId, @ApiParam("1 选择楼幢 2 楼层 3 宿舍") @RequestParam("type") Integer type, @RequestParam(value = "id", required = false) Long id) {
        return toOthersService.getBuildingInfoList(masterId, type, id);
    }

    @GetMapping("/getBuildingInfoListByIds")
    @ApiOperation(value = "获取具体统计列表")
    public PageWrapper<List<BuildingInfoListRoomVo>> getBuildingInfoListByIds(@RequestParam("masterId") Long masterId,
                                                                              @ApiParam("0是全部 1整幢楼 2整层楼 3宿舍(必传)") @RequestParam("type") Integer type,
                                                                              @RequestParam(value = "id", required = false) Long id,
                                                                              BaseQueryDto baseQueryDto) {
        return toOthersService.getBuildingInfoListByIds(masterId, type, id, baseQueryDto);
    }

    @GetMapping("/getStudentsByRoom")
    @ApiOperation(value = "获取宿舍人员")
    public Wrapper<BuildingInfoListBedVo> getStudentsByRoom(@RequestParam("roomId") Long roomId) {
        return toOthersService.getStudentsByRoom(roomId);
    }

    @GetMapping("/getStudentsOrTeachersByType")
    @ApiOperation(value = "获取教职工或学生")
    public List<Object> getStudentsOrTeachersByType(@ApiParam("学校id(必传)") @RequestParam("masterId") Long masterId, @ApiParam("类型1学生2老师(必传)") @RequestParam("type") Integer type) {
        return toOthersService.getStudentsOrTeachersByType(masterId, type);
    }

    @GetMapping("/getStudentByTeacherId")
    @ApiOperation(value = "获取某个老师班级下面的学生")
    public List<ListStudentByTeacherVo> getStudentByTeacherId(@RequestParam("teacherId") Long teacherId) {
        return toOthersService.getStudentByTeacherId(teacherId);
    }

    @GetMapping("/getStudentCountByTeacherPhone")
    @ApiOperation(value = "获取学生统计")
    public StudentCountVo getStudentCountByTeacherPhone(@RequestParam("tName") String tName, @RequestParam("teacherId") Long teacherId) {
        return toOthersService.getStudentCountByTeacherPhone(tName, teacherId);
    }

    @GetMapping("/getAllStudents")
    @ApiOperation(value = "获取所有学生")
    public PageWrapper<List<AllStudentsVo>> getAllStudents(BaseQueryDto baseQueryDto) {
        return toOthersService.getAllStudents(baseQueryDto);
    }

    @GetMapping("/getAllTeachers")
    @ApiOperation(value = "获取所有老师")
    public PageWrapper<List<AllTeachersVo>> getAllTeachers(BaseQueryDto baseQueryDto) {
        return toOthersService.getAllTeachers(baseQueryDto);
    }


    @GetMapping("getAllBuildings")
    @ApiOperation(value = "获取所有楼幢")
    public Wrapper<List<BuildingClassVo>> getAllBuildings(@RequestParam("schoolId") Long schoolId) {
        return toOthersService.getAllBuildings(schoolId);
    }


    @GetMapping("getPersonsByType")
    @ApiOperation(value = "获取学生或教师信息")
    public Wrapper<WhiteListVo> getPersonsByType(@RequestParam("schoolId") Long schoolId, @ApiParam("0学生 1教师") @RequestParam("type") Integer type) {
        return toOthersService.getPersonsByType(schoolId, type);
    }

    @GetMapping("getBuildingNoAndLevels")
    @ApiOperation(value = "获取楼幢跟楼层")
    public Wrapper<List<BuildingNoWithLevelVo>> getBuildingNoAndLevels(@RequestParam("schoolId") String schoolId) {
        return toOthersService.getBuildingNoAndLevels(schoolId);
    }

    @ApiOperation("获取学生信息 （学生档案）")
    @GetMapping("/getStudentInfoById")
    public Wrapper getStudentInfoById(@RequestParam("studentId") Long studentId) {
        return toOthersService.getStudentInfoById(studentId);
    }

//    @ApiOperation("外部同步接口")
//    @GetMapping("/info")
//    public PageWrapper<List<Object>> getStudentsOrTeachers(@RequestParam("type")Integer type, BaseQueryDto baseQueryDto, HttpServletRequest request) {
//        return toOthersService.getStudentsOrTeachers(type,baseQueryDto, request);
//    }

}

