package com.safe.campus.controller;


import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.service.ToOthersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
