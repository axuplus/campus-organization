package com.safe.campus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.model.domain.SysFileDomain;
import com.safe.campus.model.vo.FileVo;
import com.safe.campus.model.vo.SysFileVo;
import org.springframework.web.multipart.MultipartFile;

public interface SysFileService extends IService<SysFileDomain> {

    String[] IMG_TYPES = {".png", ".jpg", ".jpeg", ".gif", ".bmp",".PNG",".JPG"};

    SysFileVo fileUpload(MultipartFile file);

    FileVo getFileById(Long id);
}
