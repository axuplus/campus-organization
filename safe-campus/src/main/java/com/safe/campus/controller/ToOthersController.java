package com.safe.campus.controller;


import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.vo.OthersStudentVo;
import com.safe.campus.service.ToOthersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
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
    public List<String> getTeacherRoles(@RequestParam("teacherId") Long teacherId) {
        return toOthersService.getTeacherRoles(teacherId);
    }


    @GetMapping("/getStudentTeacherByIdNumber")
    @ApiOperation(value = "获取学生信息")
    public Map getStudentTeacherByIdNumber(@RequestParam("idNumber") String idNumber) {
        return toOthersService.getStudentTeacherByIdNumber(idNumber);
    }

    @GetMapping("/getStudentByIdNumber")
    @ApiOperation(value = "获取学生信息")
    public OthersStudentVo getStudentByIdNumber(@RequestParam("idNumber") String idNumber) {
        return toOthersService.getStudentByIdNumber(idNumber);
    }
}
