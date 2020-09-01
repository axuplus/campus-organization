package com.safe.campus.service.Impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.about.utils.EasyExcelUtil;
import com.safe.campus.about.utils.FileUtils;
import com.safe.campus.about.utils.PathUtils;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.SchoolStudentDto;
import com.safe.campus.model.dto.StudentExcelDto;
import com.safe.campus.model.vo.SchoolStudentListVo;
import com.safe.campus.model.vo.SchoolStudentVo;
import com.safe.campus.model.vo.SysFileVo;
import com.safe.campus.service.BuildingService;
import com.safe.campus.service.SchoolStudentService;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.service.SysFileService;
import org.apache.http.entity.ContentType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolStudentServiceImpl extends ServiceImpl<SchoolStudentMapper, SchoolStudent> implements SchoolStudentService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SchoolStudentMapper studentMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private SchoolClassMapper classMapper;

    @Autowired
    private SchoolClassInfoMapper infoMapper;


    @Autowired
    private BuildingStudentMapper buildingStudentMapper;

    @Autowired
    private SysFileService sysFileService;


    @Override
    public Wrapper saveStudent(SchoolStudentDto dto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isNotEmpty(dto)) {
            SchoolStudent map = new ModelMapper().map(dto, SchoolStudent.class);
            map.setId(gobalInterface.generateId());
            map.setJoinTime(dto.getJoinTime());
            map.setCreatedTime(new Date());
            map.setIsDelete(0);
            map.setCreatedUser(loginAuthDto.getUserId());
            studentMapper.insert(map);
            if (0 != dto.getBuildingBedNoId() && null != dto.getBuildingBedNoId()) {
                BuildingStudent buildingStudent = new BuildingStudent();
                buildingStudent.setId(gobalInterface.generateId());
                buildingStudent.setIsDelete(0);
                buildingStudent.setNoId(dto.getBuildingNoId());
                buildingStudent.setLevelId(dto.getBuildingLevelId());
                buildingStudent.setBedId(dto.getBuildingBedNoId());
                buildingStudent.setRoomId(dto.getBuildingRoomId());
                buildingStudent.setStudentId(map.getId());
                buildingStudent.setCreateTime(new Date());
                buildingStudentMapper.insert(buildingStudent);
            }
            return WrapMapper.ok("保存成功");
        }
        return null;
    }

    @Override
    public Wrapper getStudent(Long id) {
        if (null != id) {
            SchoolStudent byId = studentMapper.selectById(id);
            if (PublicUtil.isNotEmpty(byId)) {
                SchoolStudentVo map = new ModelMapper().map(byId, SchoolStudentVo.class);
                if (1 == byId.getSex()) {
                    map.setSex("男");
                } else {
                    map.setSex("女");
                }
                if (1 == byId.getType()) {
                    map.setType("住校生");
                    map.setLivingInfo(buildingService.getLivingInfoByStudentId(map.getId()));
                } else {
                    map.setType("同校生");
                }
                map.setImgId(sysFileService.getFileById(byId.getImgId()).getFileUrl());
                return WrapMapper.ok(map);
            }
        }
        return null;
    }

    @Override
    public Wrapper editStudent(SchoolStudentDto dto) {
        SchoolStudent map = new ModelMapper().map(dto, SchoolStudent.class);
        studentMapper.updateById(map);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper deleteStudent(Long id) {
        if (null != id) {
            studentMapper.deleteById(id);
            return WrapMapper.ok("删除成功");
        }
        return null;
    }

    @Override
    public PageWrapper<List<SchoolStudentListVo>> searchStudent(Long masterId, String context, BaseQueryDto baseQueryDto) {
        if (null != context) {
            QueryWrapper<SchoolStudent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("master_id",masterId).like("s_name", context);
            Page page = PageHelper.startPage(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
            List<SchoolStudent> students = studentMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(students)) {
                List<SchoolStudentListVo> vos = new ArrayList<>();
                students.forEach(s -> {
                    SchoolStudentListVo listVo = new SchoolStudentListVo();
                    listVo.setId(s.getId());
                    listVo.setIdNumber(s.getIdNumber());
                    listVo.setSNumber(s.getSNumber());
                    if (null != s.getClassId()) {
                        listVo.setClassName(s.getClassName());
                    }
                    if (null != s.getClassInfoName()) {
                        listVo.setClassInfoName(s.getClassInfoName());
                    }
                    if (1 == s.getSex()) {
                        listVo.setSex("男");
                    } else {
                        listVo.setSex("女");
                    }
                    if (null != s.getType()) {
                        if (1 == s.getType()) {
                            listVo.setType("住校生");
                        } else {
                            listVo.setType("同校生");
                        }
                    }
                    vos.add(listVo);
                });
                return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
            }
            return PageWrapMapper.wrap(200, "暂无数据");
        }
        return null;
    }

    @Override
    public Wrapper importSchoolConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000006);
        }
        String fileName = file.getOriginalFilename();
        if (!".xlsx".equals(PathUtils.getExtension(fileName))) {
            logger.info("文件名格式不正确,请使用后缀名为.XLSX的文件");
            throw new BizException(ErrorCodeEnum.PUB10000008);
        }
        List<StudentExcelDto> list = null;
        try {
            list = EasyExcelUtil.readExcelWithModel(file.getInputStream(), StudentExcelDto.class, ExcelTypeEnum.XLSX);
        } catch (IOException e) {
            logger.info("Excel导入失败", e);
        }
        logger.info("Excel {}", list);

        if (PublicUtil.isNotEmpty(list)) {
            list.forEach(s -> {
                SchoolStudent student = new SchoolStudent();
                student.setId(gobalInterface.generateId());
                student.setIsDelete(0);
                if (null != s.getSNumber()) {
                    student.setSNumber(s.getSNumber());
                }
                student.setSName(s.getName());
                student.setCreatedTime(new Date());
                student.setCreatedUser(loginAuthDto.getUserId());
                if (null != s.getStart()) {
                    student.setJoinTime(s.getStart());
                }
                if (null != s.getEnd()) {
                    student.setEndTime(s.getEnd());
                }
                // 检查是否相同
                checkIsSame(s.getIdNumber());
                student.setIdNumber(s.getIdNumber());
                // 检查性别
                if (null != s.getSex()) {
                    student.setSex(checkSex(s.getSex()));
                }
                // 检查年级
                student.setClassId(getThisStudentClass(s.getClassLevel()));
                student.setClassName(s.getClassLevel());
                // 检查班级
                student.setClassInfoId(getThisStudentClassInfo(s.getClassInfo()));
                student.setClassInfoName(s.getClassInfo());
                if (1 == checkStudentType(s.getType())) {
                    // 检查类型
                    student.setType(checkStudentType(s.getType()));
                    // 检查床位
                    if (null != s.getBuildingBed() && null != s.getBuildingLevel() && null != s.getBuildingRoom() && null != s.getBuildingNo()) {
                        checkBuildingInfo(s.getBuildingBed(), s.getBuildingLevel(), s.getBuildingRoom(), s.getBuildingNo());
                    }
                    // 这里固定死床位 因为学生是跟床位关联的 只有楼层或者宿舍没用 查不到 家长问题？？？
                    BuildingStudent bu = new BuildingStudent();
                    bu.setId(gobalInterface.generateId());
                    bu.setCreateTime(new Date());
                    bu.setIsDelete(0);
                    bu.setStudentId(student.getId());
                    // 全部检查确认是这幢楼下面的床位
                    Long noId = buildingService.getBuildingNoId(s.getBuildingNo());
                    Long levelId = buildingService.getBuildingLevelId(noId, s.getBuildingLevel());
                    Long roomId = buildingService.getBuildingRoomId(levelId, s.getBuildingRoom());
                    Long bedId = buildingService.getBuildingBedId(roomId, s.getBuildingBed());
                    bu.setBedId(bedId);
                    bu.setRoomId(roomId);
                    buildingStudentMapper.insert(bu);
                } else if (2 == checkStudentType(s.getType())) {
                    // 检查类型
                    student.setType(checkStudentType(s.getType()));
                }
                studentMapper.insert(student);
            });
        }

        return null;
    }

    @Override
    public List<SchoolStudent> getAllStudent(Long classInfoId) {
        QueryWrapper<SchoolStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_info_id", classInfoId);
        List<SchoolStudent> students = studentMapper.selectList(queryWrapper);
        return students;
    }

    @Override
    public SchoolStudent selectById(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public List<SchoolStudent> getAllIdsByName(String context) {
        QueryWrapper<SchoolStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("s_name", context);
        return studentMapper.selectList(queryWrapper);
    }

    @Override
    public Wrapper importStudentPictureConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000006);
        }
        String packageName = file.getOriginalFilename();
        if (packageName.matches(".*\\.zip")) {                //是zip压缩文件
            File toFile = null;
            try {
                toFile = FileUtils.multipartFileToFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //String path = toFile.getAbsolutePath();
                ZipFile zip = new ZipFile(toFile, Charset.forName("GBK"));
                for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String zipEntryName = entry.getName();
                    // 照片的命名中间以-相隔
                    if (zipEntryName.contains("-")) {
                        System.out.println("zipEntryName = " + zipEntryName);
                        // 截取zip目录之后的字符串
                        String str1 = zipEntryName.substring(0, zipEntryName.indexOf("/"));
                        String str2 = zipEntryName.substring(str1.length() + 1);
                        String name = str2.substring(0, str2.indexOf("-"));
                        System.out.println("name = " + name);
                        String str3 = str2.substring(0, zipEntryName.indexOf("-"));
                        String idNumber = zipEntryName.substring(str3.length() + 1)
                                .replace(".jpg", "")
                                .replace(".png", "")
                                .replace(".JPG", "")
                                .replace(".PNG", "");
                        System.out.println("idNumber = " + idNumber);
                        if ("".equals(name) || "".equals(idNumber)) {
                            throw new BizException(ErrorCodeEnum.PUB10000019);
                        }
                        QueryWrapper<SchoolStudent> teacherQueryWrapper = new QueryWrapper<>();
                        teacherQueryWrapper.eq("s_name", name).eq("id_number", idNumber);
                        SchoolStudent student = studentMapper.selectOne(teacherQueryWrapper);
                        if (PublicUtil.isEmpty(student)) {
                            throw new BizException(ErrorCodeEnum.PUB10000020);
                        }
                        InputStream inputStream = zip.getInputStream(entry);
                        MultipartFile multipartFile = new MockMultipartFile(str2, str2,
                                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                        SysFileVo sysFileVo = sysFileService.fileUpload(multipartFile);
                        student.setImgId(sysFileVo.getId());
                        saveOrUpdate(student);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return WrapMapper.error("暂时只支持zip压缩包");
        }
        return null;
    }

    @Override
    public PageWrapper<List<SchoolStudentListVo>> listStudent(Long masterId, Long classId, BaseQueryDto baseQueryDto) {
        if (null != masterId && null != classId) {
            QueryWrapper<SchoolStudent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("master_id",masterId).eq("class_id",classId);
            Page page = PageHelper.startPage(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
            List<SchoolStudent> students = studentMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(students)) {
                List<SchoolStudentListVo> vos = new ArrayList<>();
                students.forEach(s -> {
                    SchoolStudentListVo listVo = new SchoolStudentListVo();
                    listVo.setId(s.getId());
                    listVo.setIdNumber(s.getIdNumber());
                    listVo.setSNumber(s.getSNumber());
                    if (null != s.getClassId()) {
                        listVo.setClassName(s.getClassName());
                    }
                    if (null != s.getClassInfoName()) {
                        listVo.setClassInfoName(s.getClassInfoName());
                    }
                    if (1 == s.getSex()) {
                        listVo.setSex("男");
                    } else {
                        listVo.setSex("女");
                    }
                    if (null != s.getType()) {
                        if (1 == s.getType()) {
                            listVo.setType("住校生");
                        } else {
                            listVo.setType("同校生");
                        }
                    }
                    vos.add(listVo);
                });
                return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
            }
        }
        return null;
    }


    private void checkIsSame(String idNumber) {
        if (idNumber.length() != 18) {
            throw new BizException(ErrorCodeEnum.PUB10000022);
        }
        QueryWrapper<SchoolStudent> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("id_number", idNumber);
        SchoolStudent student = studentMapper.selectOne(studentQueryWrapper);
        if (PublicUtil.isNotEmpty(student)) {
            throw new BizException(ErrorCodeEnum.PUB10000021);
        }
    }

    private Integer checkSex(String str) {
        if ("男".equals(str)) {
            return 1;
        } else if ("女".equals(str)) {
            return 0;
        } else {
            return -1;
        }
    }

    private void checkBuildingInfo(String buildingBed, String buildingLevel, String
            buildingRoom, String buildingNo) {
        Boolean check = buildingService.checkBuildingBedIsFull(buildingBed, buildingLevel, buildingRoom, buildingNo);
        if (!check) {
            throw new BizException(ErrorCodeEnum.PUB10000023);
        }
    }

    private Long getThisStudentClass(String classLevel) {
        QueryWrapper<SchoolClass> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("class_name", classLevel);
        SchoolClass schoolClass = classMapper.selectOne(classQueryWrapper);
        if (PublicUtil.isEmpty(schoolClass)) {
            throw new BizException(ErrorCodeEnum.PUB10000025);
        }
        return schoolClass.getId();
    }

    private Long getThisStudentClassInfo(String classInfo) {
        QueryWrapper<SchoolClassInfo> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("class_info_name", classInfo);
        SchoolClassInfo schoolClassInfo = infoMapper.selectOne(classQueryWrapper);
        if (PublicUtil.isEmpty(schoolClassInfo)) {
            throw new BizException(ErrorCodeEnum.PUB10000026);
        }
        return schoolClassInfo.getId();
    }

    private Integer checkStudentType(String type) {
        if ("住校生".equals(type)) {
            return 1;
        } else if ("同校生".equals(type)) {
            return 2;
        }
        throw new BizException(ErrorCodeEnum.PUB10000024);
    }


}
