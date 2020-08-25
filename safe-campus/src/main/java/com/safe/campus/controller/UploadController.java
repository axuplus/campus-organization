package com.safe.campus.controller;

import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.model.vo.SysFileVo;
import com.safe.campus.service.SysFileService;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
@Api(value = "图片上传接口……", tags = {"图片上传接口"})
public class UploadController {


    @Autowired
    private SysFileService sysFileService;


    @ApiOperation(value = "图片上传", notes = "图片上传")
    @PostMapping(value = "/img/upload")
    public Wrapper<SysFileVo> fileUpload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(ErrorCodeEnum.PUB10000004);
        }
        SysFileVo sysFileVo = sysFileService.fileUpload(file);
        return WrapMapper.ok(sysFileVo);
    }
}
