package com.safe.campus.controller;

import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 教职工信息 前端控制器
 * </p>
 *
 * @author Joma
 * @since 2020-08-03
 */
@RestController
@RequestMapping("/campus/school/teacher")
@Api(value = "教职工管理", tags = {"教职工管理"})
public class SchoolTeacherController extends BaseController {


    @Autowired
    private SchoolTeacherService teacherService;

    @GetMapping("/list")
    @ApiOperation("获取部门下面的教师列表")
    public Wrapper listTeacherInfo(@RequestParam("id") Long id) {
        return teacherService.listTeacherInfo(id);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除部门部门下面的教师")
    public Wrapper deleteTeacherInfo(@RequestParam("id") Long id) {
        return teacherService.deleteTeacherInfo(id);
    }


    @GetMapping("/get")
    @ApiOperation("查看教职工信息")
    public Wrapper getTeacherInfo(@RequestParam("id") Long id) {
        return teacherService.getTeacherInfo(id);
    }


    @PostMapping("/save")
    @ApiOperation("添加")
    public Wrapper saveTeacherInfo(@RequestBody TeacherInfoDto teacherInfoDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return teacherService.saveTeacherInfo(teacherInfoDto, loginAuthDto);
    }


    @PutMapping("/edit")
    @ApiOperation("修改")
    public Wrapper editTeacherInfo(@RequestBody TeacherInfoDto teacherInfoDto) {
        return teacherService.editTeacherInfo(teacherInfoDto);
    }

    @GetMapping("/search")
    @ApiOperation("搜索")
    public Wrapper searchTeacherInfo(@RequestParam("context") String context) {
        return teacherService.searchTeacherInfo(context);
    }


    @PostMapping("/import/teacher")
    @ApiOperation("导入")
    public Wrapper importTeacherConcentrator(@ApiParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) throws Exception {
        return teacherService.importTeacherConcentrator(file, getLoginAuthDto());
    }

    @PostMapping("/import/teacher/pictures")
    @ApiOperation("导入照片")
    public Wrapper importTeacherPictureConcentrator(@ApiParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) throws Exception {
        return teacherService.importTeacherPictureConcentrator(file, getLoginAuthDto());
    }


    @GetMapping("/listRoles")
    @ApiOperation("添加教师的角色列表")
    public Wrapper listRoles() {
        return teacherService.listRoles(getLoginAuthDto());
    }


    @PostMapping("/setRole")
    @ApiOperation("给教师设置角色")
    public Wrapper setRole(@RequestBody SetRoleDto setRoleDto) {
        return teacherService.setRole(getLoginAuthDto(), setRoleDto);
    }

}
