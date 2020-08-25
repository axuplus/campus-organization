package com.safe.campus.controller;


import com.safe.campus.about.controller.BaseController;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.model.dto.SchoolStudentDto;
import com.safe.campus.service.SchoolStudentService;
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
 * 学生表 前端控制器
 * </p>
 * @author Joma
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/campus/school/student")
@Api(value = "学生管理", tags = {"学生管理"})
public class SchoolStudentController extends BaseController {


    @Autowired
    private SchoolStudentService schoolStudentService;


    @ApiOperation("添加学生")
    @PostMapping("/save")
    public Wrapper saveStudent(@RequestBody SchoolStudentDto dto){
        return schoolStudentService.saveStudent(dto);
    }

    @ApiOperation("获取学生")
    @GetMapping("/get")
    public Wrapper getStudent(@RequestParam("id")Long id){
        return schoolStudentService.getStudent(id);
    }

    @ApiOperation("修改学生")
    @PutMapping("/edit")
    public Wrapper editStudent(@RequestBody SchoolStudentDto dto){
        return schoolStudentService.editStudent(dto);
    }

    @ApiOperation("删除学生")
    @DeleteMapping("/delete/{id}")
    public Wrapper deleteStudent(@PathVariable("id")Long id){
        return schoolStudentService.deleteStudent(id);
    }

    @ApiOperation("搜索学生")
    @GetMapping("/search")
    public Wrapper searchStudent(@RequestParam("context")String context){
        return schoolStudentService.searchStudent(context);
    }

    @PostMapping("/import/student")
    @ApiOperation("导入excel")
    public Wrapper importStudentConcentrator(@ApiParam(value = "file", required = true) MultipartFile file) throws Exception {
        return schoolStudentService.importSchoolConcentrator(file,getLoginAuthDto());
    }

    @PostMapping("/import/student/picture")
    @ApiOperation("导入照片")
    public Wrapper importStudentPictureConcentrator(@ApiParam(value = "file", required = true) MultipartFile file) throws Exception {
        return schoolStudentService.importStudentPictureConcentrator(file,getLoginAuthDto());
    }

}
