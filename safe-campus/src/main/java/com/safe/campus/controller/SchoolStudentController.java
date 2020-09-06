package com.safe.campus.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.annotation.PermissionType;
import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.dto.SchoolStudentDto;
import com.safe.campus.model.vo.SchoolStudentListVo;
import com.safe.campus.service.SchoolStudentService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 * @author Joma
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/campus/school/student")
@Api(value = "学生管理", tags = {"学生管理"})
public class SchoolStudentController extends BaseController {

    private final static String qxurl = "/campus/school/student";



    @Autowired
    private SchoolStudentService schoolStudentService;


    @Permission(url = qxurl,type = PermissionType.ADD)
    @ApiOperation("添加学生")
    @PostMapping("/save")
    public Wrapper saveStudent(@RequestBody SchoolStudentDto dto){
        return schoolStudentService.saveStudent(dto,getLoginAuthDto());
    }

    @ApiOperation("获取学生")
    @GetMapping("/get")
    public Wrapper getStudent(@RequestParam("id")Long id){
        return schoolStudentService.getStudent(id);
    }

    @Permission(url = qxurl,type = PermissionType.EDIT)
    @ApiOperation("修改学生")
    @PutMapping("/edit")
    public Wrapper editStudent(@RequestBody SchoolStudentDto dto){
        return schoolStudentService.editStudent(dto);
    }

    @Permission(url = qxurl,type = PermissionType.DEL)
    @ApiOperation("删除学生")
    @DeleteMapping("/delete/{id}")
    public Wrapper deleteStudent(@PathVariable("id")Long id){
        return schoolStudentService.deleteStudent(id);
    }

    @Permission(url = qxurl,type = PermissionType.QUERY)
    @ApiOperation("搜索学生")
    @GetMapping("/search")
    public PageWrapper<List<SchoolStudentListVo>> searchStudent(@RequestParam("masterId")Long masterId, @RequestParam("context")String context, BaseQueryDto baseQueryDto){
        return schoolStudentService.searchStudent(masterId,context,baseQueryDto);
    }

    @Permission(url = qxurl,type = PermissionType.QUERY)
    @ApiOperation("学生列表")
    @GetMapping("/list")
    public PageWrapper<List<SchoolStudentListVo>> listStudent(@ApiParam("1全部2年级3班级")@RequestParam("type")Integer type,
                                                              @RequestParam("masterId")Long masterId,
                                                              @ApiParam("type=2的话就是年级 3的话就是班级id")@RequestParam("id")Long id,
                                                              BaseQueryDto baseQueryDto){
        return schoolStudentService.listStudent(type,masterId,id,baseQueryDto);
    }

    @Permission(url = qxurl,type = PermissionType.ADD)
    @PostMapping("/import/student")
    @ApiOperation("导入excel")
    public Wrapper importStudentConcentrator(@RequestParam("masterId")Long masterId,@ApiParam(value = "file", required = true) MultipartFile file) throws Exception {
        return schoolStudentService.importSchoolConcentrator(masterId,file,getLoginAuthDto());
    }

    @Permission(url = qxurl,type = PermissionType.ADD)
    @PostMapping("/import/student/picture")
    @ApiOperation("导入照片")
    public Wrapper importStudentPictureConcentrator(@Param("masterId") Long masterId, @ApiParam(value = "file", required = true) MultipartFile file) throws Exception {
        return schoolStudentService.importStudentPictureConcentrator(masterId,file,getLoginAuthDto());
    }

}
