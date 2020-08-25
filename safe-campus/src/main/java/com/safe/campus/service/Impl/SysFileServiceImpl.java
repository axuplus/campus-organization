package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.mapper.SysFileMapper;
import com.safe.campus.model.domain.SysFileDomain;
import com.safe.campus.model.vo.FileVo;
import com.safe.campus.model.vo.SysFileVo;
import com.safe.campus.service.SysFileService;
import com.safe.campus.about.utils.PathUtils;
import com.safe.campus.about.utils.PictureUtils;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.StringUtils;
import com.safe.campus.about.utils.service.GobalInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;


@Service
@Transactional(rollbackFor = Exception.class)
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper,SysFileDomain> implements SysFileService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Value(value = "${upload-file.imgPath}")
    private String imgPath;

    @Value(value = "${upload-file.imgUrl}")
    private String imgUrl;

    @Value(value = "${upload-file.imgThumbPath}")
    private String imgThumbPath;

    @Value(value = "${upload-file.imgThumbUrl}")
    private String imgThumbUrl;



    @Override
    public SysFileVo fileUpload(MultipartFile file) {
        SysFileVo sysFileVo = new SysFileVo();
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000004);
        }
        logger.info("imgPath {}", imgPath);
        String fileName = file.getOriginalFilename();
        logger.info("fileName {}", fileName);
        String suffix = PathUtils.getExtension(fileName);

        String filePath;
        String thumbName;
        String fileUrl;
        String thumbFilePath;
        String thumbUrl;

        fileName = StringUtils.getRandomString(30) + suffix;
        thumbName = StringUtils.getRandomString(30) + ".png";

        if (!Arrays.asList(IMG_TYPES).contains(suffix)) {
            throw new BizException(ErrorCodeEnum.PUB10000005);
        }
        filePath = imgPath + File.separator + fileName;

        File dest = new File(filePath);
        logger.info("save dest {}", dest.getPath());
        try {
            file.transferTo(dest);
            fileUrl = imgUrl + fileName;
            thumbFilePath = imgThumbPath + File.separator + thumbName;
            thumbUrl = imgThumbUrl + thumbName;
            PictureUtils.thumbnailWithScale(filePath, thumbFilePath, 0.25f);
            SysFileDomain filesDomain = new SysFileDomain();
            filesDomain.setId(gobalInterface.generateId());
            filesDomain.setFileName(fileName);
            filesDomain.setFileSize(file.getSize());
            filesDomain.setFilePath(filePath);
            filesDomain.setFileSuffix(suffix);
            filesDomain.setFileUrl(fileUrl);
            filesDomain.setFileType(1);
            filesDomain.setThumbName(thumbName);
            filesDomain.setThumbPath(thumbFilePath);
            filesDomain.setThumbUrl(thumbUrl);
            filesDomain.setCreateTime(new Date());
            sysFileMapper.insert(filesDomain);
            sysFileVo.setId(filesDomain.getId());
            sysFileVo.setFileUrl(fileUrl);
            sysFileVo.setThumbUrl(thumbUrl);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new BizException(ErrorCodeEnum.PUB10000001);
        }
        return sysFileVo;
    }


    @Override
    public FileVo getFileById(Long id) {
        FileVo fileVo = new FileVo();
        SysFileDomain sysFileDomain = sysFileMapper.selectById(id);
        if (PublicUtil.isNotEmpty(sysFileDomain)) {
            fileVo.setThumbUrl(sysFileDomain.getThumbUrl());
            fileVo.setFileUrl(sysFileDomain.getFileUrl());
        }
        return fileVo;
    }

}
