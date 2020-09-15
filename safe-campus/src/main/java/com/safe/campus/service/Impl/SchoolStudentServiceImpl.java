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
import com.safe.campus.model.dto.BuildingNoMapperDto;
import com.safe.campus.model.dto.SchoolStudentDto;
import com.safe.campus.model.dto.StudentExcelDto;
import com.safe.campus.model.vo.*;
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
import java.util.*;
import java.util.stream.Collectors;
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
    public Wrapper<SchoolStudentVo> getStudent(Long id) {
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
                map.setPhoto(sysFileService.getFileById(byId.getImgId()).getFileUrl());
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
            queryWrapper.eq("master_id", masterId).like("s_name", context);
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
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
                return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
            return PageWrapMapper.wrap(200, "暂无数据");
        }
        return null;
    }

    @Override
    public Wrapper importSchoolConcentrator(Long masterId, MultipartFile file, LoginAuthDto loginAuthDto) {
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
            QueryWrapper<SchoolStudent> studentQueryWrapper = new QueryWrapper<>();
            studentQueryWrapper.eq("master_id", masterId);
            List<SchoolStudent> students = studentMapper.selectList(studentQueryWrapper);
            Map<String, Long> maps = students.stream().collect(Collectors.toMap(SchoolStudent::getIdNumber, SchoolStudent::getId));
            list.forEach(s -> {
                SchoolStudent student = new SchoolStudent();
                Long id = maps.get(s.getIdNumber());
                // 更新
                if (null != id) {
                    student = studentMapper.selectById(id);
                    // 新增
                } else {
                    student.setId(gobalInterface.generateId());
                    student.setMasterId(masterId);
                    student.setIdNumber(checkIdNumber(s.getIdNumber()));
                    student.setSName(s.getName());
                    student.setCreatedTime(new Date());
                    student.setCreatedUser(loginAuthDto.getUserId());
                    student.setIsDelete(0);
                }
                if (null != s.getStudentNumber()) {
                    student.setSNumber(s.getStudentNumber());
                }
                if (null != s.getStart()) {
                    String time = EasyExcelUtil.formatExcelDate(Integer.valueOf(s.getStart()));
                    student.setJoinTime(time);
                }
                if (null != s.getEnd()) {
                    String time = EasyExcelUtil.formatExcelDate(Integer.valueOf(s.getEnd()));
                    student.setEndTime(time);
                }
                // 检查性别
                if (null != s.getSex()) {
                    student.setSex(checkSex(s.getSex()));
                }
                // 检查年级
                if (null != s.getClassLevel()) {
                    student.setClassId(getThisStudentClass(s.getClassLevel(), masterId));
                    student.setClassName(s.getClassLevel());
                }
                // 检查班级
                if (null != s.getClassInfo()) {
                    student.setClassInfoId(getThisStudentClassInfo(s.getClassInfo()));
                    student.setClassInfoName(s.getClassInfo());
                }
                if (null != s.getType()) {
                    if (1 == checkStudentType(s.getType())) {
                        student.setType(1);
                        // 检查床位
                        if (null != s.getBuildingBed() && null != s.getBuildingLevel() && null != s.getBuildingRoom() && null != s.getBuildingNo()) {
                            BuildingNoMapperDto buildingNoMapperDto = checkBuildingInfo(masterId, s.getBuildingNo(), s.getBuildingLevel(), s.getBuildingRoom(), s.getBuildingBed());
                            if (PublicUtil.isEmpty(buildingNoMapperDto)) {
                                throw new BizException(ErrorCodeEnum.PUB10000033);
                            }
                            if (checkThisBedHasTaken(buildingNoMapperDto)) {
                                BuildingStudent bu = new BuildingStudent();
                                bu.setId(gobalInterface.generateId());
                                bu.setCreateTime(new Date());
                                bu.setIsDelete(0);
                                bu.setStudentId(student.getId());
                                bu.setNoId(buildingNoMapperDto.getNoId());
                                bu.setLevelId(buildingNoMapperDto.getLevelId());
                                bu.setRoomId(buildingNoMapperDto.getRoomId());
                                bu.setBedId(buildingNoMapperDto.getBedId());
                                buildingStudentMapper.insert(bu);
                            }
                        }
                    } else {
                        student.setType(2);
                    }
                }
                saveOrUpdate(student);
            });
        }
        return WrapMapper.ok("导入成功");
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
    public Wrapper importStudentPictureConcentrator(Long masterId, MultipartFile file, LoginAuthDto loginAuthDto) {
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
                        QueryWrapper<SchoolStudent> studentQueryWrapper = new QueryWrapper<>();
                        studentQueryWrapper.eq("s_name", name).eq("id_number", idNumber).eq("master_id", masterId);
                        SchoolStudent student = studentMapper.selectOne(studentQueryWrapper);
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
        return WrapMapper.ok("导入成功");
    }

    @Override
    public PageWrapper<List<SchoolStudentListVo>> listStudent(Integer type, Long masterId, Long id, BaseQueryDto baseQueryDto) {
        QueryWrapper<SchoolStudent> queryWrapper = new QueryWrapper<>();
        if (1 == type) {
            queryWrapper.eq("master_id", masterId).orderByDesc("created_time");
        } else if (2 == type) {
            queryWrapper.eq("master_id", masterId).eq("class_id", id).orderByDesc("created_time");
        } else if (3 == type) {
            queryWrapper.eq("master_id", masterId).eq("class_info_id", id).orderByDesc("created_time");
        }
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SchoolStudent> students = studentMapper.selectList(queryWrapper);
        Long total = page.getTotal();
        if (PublicUtil.isNotEmpty(students)) {
            List<SchoolStudentListVo> vos = new ArrayList<>();
            students.forEach(s -> {
                SchoolStudentListVo listVo = new SchoolStudentListVo();
                listVo.setId(s.getId());
                listVo.setSName(s.getSName());
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
            return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public Wrapper<List<SchoolStudentBuildingVo>> getStudentBuildingInfo(Long masterId, Integer type, Long id) {
        List<SchoolStudentBuildingVo> vos = buildingService.getStudentBuildingInfo(masterId, type, id);
        if (PublicUtil.isNotEmpty(vos)) {
            return WrapMapper.ok(vos);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<List<SchoolClassStudentVo>> getSchoolClassStudent(Long masterId, Long id) {
        List<SchoolClassStudentVo> vos = new ArrayList<>();
        if (null == id) {
            // 年级
            List<SchoolClass> classes = classMapper.selectList(new QueryWrapper<SchoolClass>().eq("master_id", masterId));
            if (PublicUtil.isNotEmpty(classes)) {
                classes.forEach(c -> {
                    SchoolClassStudentVo schoolClassStudentVo = new SchoolClassStudentVo();
                    schoolClassStudentVo.setId(c.getId());
                    schoolClassStudentVo.setType(1);
                    schoolClassStudentVo.setName(c.getClassName());
                    vos.add(schoolClassStudentVo);
                });
            }
        } else {
            List<SchoolClassInfo> infos = infoMapper.selectList(new QueryWrapper<SchoolClassInfo>().eq("class_id", id));
            if (PublicUtil.isNotEmpty(infos)) {
                infos.forEach(i -> {
                    SchoolClassStudentVo schoolClassStudentVo = new SchoolClassStudentVo();
                    schoolClassStudentVo.setId(i.getId());
                    schoolClassStudentVo.setType(2);
                    schoolClassStudentVo.setName(i.getClassInfoName());
                    vos.add(schoolClassStudentVo);
                });
            }
        }
        return WrapMapper.ok(vos);
    }


    private String checkIdNumber(String idNumber) {
        if (idNumber.length() != 18) {
            throw new BizException(ErrorCodeEnum.PUB10000022);
        }
        return idNumber;
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

    private BuildingNoMapperDto checkBuildingInfo(Long masterId, String buildingNo, String buildingLevel, String
            buildingRoom, String buildingBed) {
        return buildingService.checkBuildingInfo(masterId, buildingNo, buildingLevel, buildingRoom, buildingBed);
    }

    private Long getThisStudentClass(String classLevel, Long masterId) {
        QueryWrapper<SchoolClass> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("class_name", classLevel).eq("master_id", masterId);
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
        System.out.println("type = " + type);
        if ("住校生".equals(type)) {
            return 1;
        } else if ("通校生".equals(type)) {
            return 2;
        }
        throw new BizException(ErrorCodeEnum.PUB10000024);
    }

    private Boolean checkThisBedHasTaken(BuildingNoMapperDto buildingNoMapperDto) {
        QueryWrapper<BuildingStudent> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("no_id", buildingNoMapperDto.getNoId())
                .eq("level_id", buildingNoMapperDto.getLevelId())
                .eq("room_id", buildingNoMapperDto.getRoomId())
                .eq("bed_id", buildingNoMapperDto.getBedId());
        BuildingStudent buildingStudent = buildingStudentMapper.selectOne(studentQueryWrapper);
        if (PublicUtil.isNotEmpty(buildingStudent)) {
            throw new BizException(ErrorCodeEnum.PUB10000023);
        }
        return true;
    }

}
