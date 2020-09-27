package com.safe.campus.controller;


import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.vo.FaceImgInfoVo;
import com.safe.campus.model.vo.OthersStudentVo;
import com.safe.campus.model.vo.OthersTeacherVo;
import com.safe.campus.service.ToOthersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public FaceImgInfoVo getFaceImgInfo(@ApiParam("S T") @RequestParam("type") String type, @RequestParam("id") Long id) {
        return toOthersService.getFaceImgInfo(type, id);
    }
}
