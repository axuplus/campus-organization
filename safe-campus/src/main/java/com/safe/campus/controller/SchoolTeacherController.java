package com.safe.campus.controller;

import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.model.vo.SchoolTeacherVo;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    private final static String qxurl = "/campus/school/teacher";


    @Autowired
    private SchoolTeacherService teacherService;

    @Permission(url = qxurl, type = PermissionType.ADD)
    @PostMapping("/save")
    @ApiOperation("添加")
    public Wrapper saveTeacherInfo(@RequestBody TeacherInfoDto teacherInfoDto) {
        LoginAuthDto loginAuthDto = getLoginAuthDto();
        return teacherService.saveTeacherInfo(teacherInfoDto, loginAuthDto);
    }


    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/list")
    @ApiOperation("获取某个部门下面的教师列表")
    public PageWrapper<List<SchoolTeacherVo>> listTeacherInfo(@ApiParam("1全部2部门")@RequestParam(value = "type",required = true)Integer type,
                                                              @RequestParam("masterId") Long masterId,
                                                              @RequestParam("sectionId") Long id,
                                                              BaseQueryDto baseQueryDto) {
        return teacherService.listTeacherInfo(type,masterId, id, baseQueryDto);
    }


    @Permission(url = qxurl, type = PermissionType.DEL)
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


    @Permission(url = qxurl, type = PermissionType.EDIT)
    @PutMapping("/edit")
    @ApiOperation("修改")
    public Wrapper editTeacherInfo(@RequestBody TeacherInfoDto teacherInfoDto) {
        return teacherService.editTeacherInfo(teacherInfoDto);
    }

    @Permission(url = qxurl, type = PermissionType.QUERY)
    @GetMapping("/search")
    @ApiOperation("搜索")
    public PageWrapper<List<SchoolTeacherVo>> searchTeacherInfo(@RequestParam("masterId") Long masterId, @RequestParam("context") String context, BaseQueryDto baseQueryDto) {
        return teacherService.searchTeacherInfo(masterId, context, baseQueryDto);
    }


    @Permission(url = qxurl,type = PermissionType.ADD)
    @PostMapping("/import/teacher")
    @ApiOperation("导入")
    public Wrapper importTeacherConcentrator(@ApiParam(value = "file", required = true) MultipartFile file,@RequestParam("masterId")Long masterId, HttpServletRequest request) throws Exception {
        return teacherService.importTeacherConcentrator(file, masterId,getLoginAuthDto());
    }

    @Permission(url = qxurl,type = PermissionType.ADD)
    @PostMapping("/import/teacher/pictures")
    @ApiOperation("导入照片")
    public Wrapper importTeacherPictureConcentrator(@ApiParam(value = "file", required = true) MultipartFile file,@RequestParam("masterId")Long masterId, HttpServletRequest request) throws Exception {
        return teacherService.importTeacherPictureConcentrator(file,masterId, getLoginAuthDto());
    }


    @GetMapping("/listRoles")
    @ApiOperation("添加教师的角色列表")
    public Wrapper listRoles(@RequestParam("masterId") Long masterId) {
        return teacherService.listRoles(masterId, getLoginAuthDto());
    }


    @Permission(url = qxurl, type = PermissionType.SET)
    @PostMapping("/setRole")
    @ApiOperation("给教师设置角色")
    public Wrapper setRole(@RequestBody SetRoleDto setRoleDto) {
        return teacherService.setRole(getLoginAuthDto(), setRoleDto);
    }

    @Permission(url = qxurl, type = PermissionType.ACTIVE)
    @GetMapping("/active")
    @ApiOperation("停用/启用")
    public Wrapper<Map<Long, String>> active(@ApiParam("此教师的ID") @RequestParam("id") Long id,
                                             @RequestParam("masterId") Long masterId,
                                             @ApiParam("1:停用 0:启用")@RequestParam("state") Integer state) {
        return teacherService.active(getLoginAuthDto(), id, masterId, state);
    }

}
