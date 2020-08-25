package com.safe.campus.service.Impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.*;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherExcelDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.model.vo.SchoolTeacherVo;
import com.safe.campus.model.vo.SysFileVo;
import com.safe.campus.model.vo.SysRoleVo;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.service.SysFileService;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import org.apache.http.entity.ContentType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * 教职工信息 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolTeacherServiceImpl extends ServiceImpl<SchoolTeacherMapper, SchoolTeacher> implements SchoolTeacherService {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SchoolTeacherMapper teacherMapper;

    @Autowired
    private SchoolClassMapper classMapper;

    @Autowired
    private SchoolClassInfoMapper classInfoMapper;

    @Autowired
    private SchoolSectionMapper sectionMapper;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SysAdminUserMapper adminUserMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysFileService fileService;


    @Override
    public Wrapper listTeacherInfo(Long id) {
        if (null == id) {
            return WrapMapper.error("参数不能为空");
        }
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id", id);
        List<SchoolTeacher> teachers = teacherMapper.selectList(queryWrapper);
        if (PublicUtil.isNotEmpty(teachers)) {
            List<SchoolTeacherVo> list = new ArrayList<>();
            teachers.forEach(teacher -> {
                SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
                // 关联班级
                if (null != teacher.getClassId() && null != teacher.getClassInfoId()) {
                    SchoolClass schoolClass = classMapper.selectById(teacher.getClassId());
                    SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(teacher.getClassInfoId());
                    vo.setClassInformation(schoolClass.getClassName() + " " + schoolClassInfo.getClassInfoName());
                }
                // 部门
                SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
                if (null != section) {
                    vo.setSectionName(section.getSectionName());
                }
                // 此教职工下面关联的账号
                QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_id", teacher.getAdminId());
                List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                if (PublicUtil.isNotEmpty(userRoles)) {
                    List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> roles = roleMapper.selectBatchIds(ids);
                    // 此账号下面关联的多个角色
                    vo.setRoleInfo(roles.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName)));
                }
                list.add(vo);
            });
            return WrapMapper.ok(list);
        }
        return null;
    }

    @Override
    public Wrapper deleteTeacherInfo(Long id) {
        if (null == id) {
            return WrapMapper.error("参数不能为空");
        }
        // 先删除关联账号
        adminUserMapper.deleteById(teacherMapper.selectById(id).getAdminId());
        teacherMapper.deleteById(id);
        return WrapMapper.ok("删除成功");
    }

    @Override
    public Wrapper getTeacherInfo(Long id) {
        if (null == id) {
            return WrapMapper.error("参数不能为空");
        }
        SchoolTeacher teacher = teacherMapper.selectById(id);
        if (PublicUtil.isNotEmpty(teacher)) {
            SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
            SchoolClass schoolClass = classMapper.selectById(teacher.getClassId());
            SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(teacher.getClassInfoId());
            if (null != schoolClass && null != schoolClassInfo) {
                vo.setClassInformation(schoolClass.getClassName() + " " + schoolClassInfo.getClassInfoName());
            }
            SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
            if (null != section) {
                vo.setSectionName(section.getSectionName());
            }
            vo.setPhoto(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            // 此教职工下面关联的账号
            QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", teacher.getAdminId());
            List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
            if (PublicUtil.isNotEmpty(userRoles)) {
                List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                List<SysRole> roles = roleMapper.selectBatchIds(ids);
                // 此账号下面关联的多个角色
                // vo.setRoleId(roles.stream().map(SysRole::getId).collect(Collectors.toList()));
                // vo.setRoleName(roles.stream().map(SysRole::getRoleName).collect(Collectors.toList()));
                vo.setRoleInfo(roles.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName)));
            }
            return WrapMapper.ok(vo);
        }
        return null;
    }

    @Override
    public Wrapper saveTeacherInfo(TeacherInfoDto teacherInfoDto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isEmpty(teacherInfoDto)) {
            return WrapMapper.error("信息不能为空");
        }
        SchoolTeacher teacher = new SchoolTeacher();
        teacher.setId(gobalInterface.generateId());
        teacher.setClassId(teacherInfoDto.getClassId());
        teacher.setClassInfoId(teacherInfoDto.getClassInfoId());
        teacher.setCreatedTime(new Date());
        teacher.setTName(teacherInfoDto.getTName());
        teacher.setImgId(teacherInfoDto.getFaceInfoId());
        teacher.setIdNumber(teacherInfoDto.getIdNumber());
        teacher.setIsDelete(0);
        teacher.setPhone(teacherInfoDto.getPhone());
        teacher.setJoinTime(teacherInfoDto.getJoinTime());
        teacher.setPosition(teacherInfoDto.getPosition());
        teacher.setSectionId(teacherInfoDto.getSectionId());
        teacher.setSex(teacherInfoDto.getSex());
        teacher.setState(0);
        teacher.setTNumber(teacherInfoDto.getTNumber());
        teacher.setCreatedUser(loginAuthDto.getUserId());
        // admin插入
        SysAdmin admin = new SysAdmin();
        admin.setId(gobalInterface.generateId());
        admin.setState(0);
        admin.setLevel(3);
        admin.setType(3);
        admin.setCreateTime(new Date());
        admin.setUserName(teacher.getPhone().toString());
        admin.setPassword(Md5Utils.md5Str(teacher.getIdNumber().substring(teacher.getIdNumber().length() - 6)));
        admin.setAppKey(StringUtils.getRandomString(7).toUpperCase());
        admin.setAppSecret(StringUtils.getRandomString(13).toLowerCase());
        admin.setCreateUser(loginAuthDto.getUserId());
        // PID给个默认
        admin.setPId(1L);
        adminUserMapper.insert(admin);
        teacher.setAdminId(admin.getId());
        teacherMapper.insert(teacher);
        // 教职工角色设置
        if (PublicUtil.isNotEmpty(teacherInfoDto.getRoleId())) {
            teacherInfoDto.getRoleId().forEach(i -> {
                SysUserRole role = new SysUserRole();
                role.setId(gobalInterface.generateId());
                role.setRoleId(i);
                role.setUserId(admin.getId());
                userRoleMapper.insert(role);
            });
        }
        return WrapMapper.ok("保存成功");
    }

    @Override
    public Wrapper editTeacherInfo(TeacherInfoDto teacherInfoDto) {
        if (PublicUtil.isEmpty(teacherInfoDto)) {
            return WrapMapper.error("修改参数不能为空");
        }
        SchoolTeacher teacher = teacherMapper.selectById(teacherInfoDto.getId());
        teacher.setTName(teacherInfoDto.getTName());
        teacher.setClassId(teacherInfoDto.getClassId());
        teacher.setClassInfoId(teacherInfoDto.getClassInfoId());
        teacher.setImgId(teacherInfoDto.getFaceInfoId());
        teacher.setIdNumber(teacherInfoDto.getIdNumber());
        teacher.setPhone(teacherInfoDto.getPhone());
        teacher.setJoinTime(teacherInfoDto.getJoinTime());
        teacher.setPosition(teacherInfoDto.getPosition());
        teacher.setSectionId(teacherInfoDto.getSectionId());
        teacher.setSex(teacherInfoDto.getSex());
        teacher.setTNumber(teacherInfoDto.getTNumber());
        // 教职工角色更新
        userRoleMapper.deleteByAdminId(teacher.getAdminId());
        if (PublicUtil.isNotEmpty(teacherInfoDto.getRoleId())) {
            teacherInfoDto.getRoleId().forEach(i -> {
                SysUserRole role = new SysUserRole();
                role.setId(gobalInterface.generateId());
                role.setRoleId(i);
                System.out.println("teacher.getAdminId() = " + teacher.getAdminId());
                role.setUserId(teacher.getAdminId());
                userRoleMapper.insert(role);
            });
        }
        teacherMapper.updateById(teacher);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper searchTeacherInfo(String context) {
        if ("".equals(context)) {
            return WrapMapper.error("搜索条件不能为空");
        }
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("t_name", context).or()
                .like("t_number", context).or()
                .like("phone", context).or()
                .like("join_time", context)
                .orderByAsc("created_time");
        List<SchoolTeacher> teachers = teacherMapper.selectList(queryWrapper);
        List<SchoolTeacherVo> list = new ArrayList<>();
        if (PublicUtil.isNotEmpty(teachers)) {
            teachers.forEach(teacher -> {
                SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
                // 关联班级
                SchoolClass schoolClass = classMapper.selectById(teacher.getClassId());
                SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(teacher.getClassInfoId());
                if (null != schoolClass && null != schoolClassInfo) {
                    vo.setClassInformation(schoolClass.getClassName() + " " + schoolClassInfo.getClassInfoName());
                }
                // 部门
                SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
                if (null != section) {
                    vo.setSectionName(section.getSectionName());
                }
                // 此教职工下面关联的账号
                QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_id", teacher.getAdminId());
                List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                if (PublicUtil.isNotEmpty(userRoles)) {
                    List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> roles = roleMapper.selectBatchIds(ids);
                    // 此账号下面关联的多个角色
                    vo.setRoleInfo(roles.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName)));
                }
                list.add(vo);
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无数据");
    }


    /**
     * excel导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Wrapper importTeacherConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000006);
        }
        String fileName = file.getOriginalFilename();
        if (!".xlsx".equals(PathUtils.getExtension(fileName))) {
            logger.info("文件名格式不正确,请使用后缀名为.XLSX的文件");
            throw new BizException(ErrorCodeEnum.PUB10000008);
        }
        List<TeacherExcelDto> list = null;
        try {
            list = EasyExcelUtil.readExcelWithModel(file.getInputStream(), TeacherExcelDto.class, ExcelTypeEnum.XLSX);
        } catch (IOException e) {
            logger.info("Excel导入失败", e);
        }
        logger.info("Excel {}", list);
        // 循环导入
        if (PublicUtil.isNotEmpty(list)) {
            list.forEach(t -> {
                SchoolTeacher teacher = new SchoolTeacher();
                if (-1 == checkSex(t.getSex())) {
                    throw new BizException(ErrorCodeEnum.PUB10000012);
                }
                // 插入
                teacher.setId(gobalInterface.generateId());
                teacher.setTName(t.getName());
                teacher.setIdNumber(checkIsSame(t.getIdNumber()));
                teacher.setCreatedTime(new Date());
                teacher.setIsDelete(0);
                teacher.setPhone(Long.valueOf(t.getPhone()));
                teacher.setSectionId(checkSectionId(t.getSectionName()));
                teacher.setCreatedUser(loginAuthDto.getUserId());
                // 去判断非空字段
                if (null != t.getSex()) {
                    teacher.setSex(checkSex(t.getSex()));
                }
                if (null != t.getTNumber()) {
                    teacher.setTNumber(t.getTNumber());
                }
                if (null != t.getJoinTime()) {
                    teacher.setJoinTime(t.getJoinTime());
                }
                if (null != t.getPositionName()) {
                    teacher.setPosition(t.getPositionName());
                }
                if (null != t.getShape()) {
                    if ("在职".equals(t.getShape())) {
                        teacher.setState(0);
                    } else if ("离职".equals(t.getShape())) {
                        teacher.setState(1);
                    } else {
                        throw new BizException(ErrorCodeEnum.PUB10000018);
                    }
                }
//                // admin插入
//                SysAdmin admin = new SysAdmin();
//                admin.setId(gobalInterface.generateId());
//                admin.setState(0);
//                admin.setLevel(3);
//                admin.setType(3);
//                admin.setCreateTime(new Date());
//                admin.setUserName(teacher.getPhone().toString());
//                admin.setPassword(Md5Utils.md5Str(teacher.getIdNumber().substring(teacher.getIdNumber().length() - 6)));
//                admin.setAppKey(StringUtils.getRandomString(7).toUpperCase());
//                admin.setAppSecret(StringUtils.getRandomString(13).toLowerCase());
//                admin.setCreateUser(loginAuthDto.getUserId());
//                admin.setPId(1L);
//                adminUserMapper.insert(admin);
                // 分割角色
//                if (null != t.getRoleName()) {
//                    List<String> roles = Arrays.asList(t.getRoleName().split("/"));
//                    for (String role : roles) {
//                        Long checkIdByRoleName = roleMapper.checkIdByRoleName(role);
//                        if (null == checkIdByRoleName) {
//                            throw new BizException(ErrorCodeEnum.PUB10000011);
//                        } else {
//                            // 分配角色到 user_role
//                            SysUserRole userRole = new SysUserRole();
//                            userRole.setId(gobalInterface.generateId());
//                            userRole.setUserId(teacher.getId());
//                            userRole.setRoleId(checkIdByRoleName);
//                            userRoleMapper.insert(userRole);
//                        }
//                    }
//                }
//                teacher.setAdminId(admin.getId());
                teacherMapper.insert(teacher);
            });
            return WrapMapper.ok("导入成功");
        }
        return WrapMapper.error("位置错误");
    }

    /**
     * 检查是否有重复的人员
     */
    private String checkIsSame(String idNumber) {
        if(idNumber.length() != 18){
            throw new BizException(ErrorCodeEnum.PUB10000022);
        }
        QueryWrapper<SchoolTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("id_number", idNumber);
        SchoolTeacher teacher = teacherMapper.selectOne(wrapper);
        if (null != teacher) {
            logger.info("表格有身份证号码相同的人员 {}", teacher.getIdNumber());
            throw new BizException(ErrorCodeEnum.PUB10000009);
        }
        return idNumber;
    }

    /**
     * 检查男女
     */
    private Integer checkSex(String str) {
        if ("男".equals(str)) {
            return 1;
        } else if ("女".equals(str)) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 检查导入部门是否合法
     */
    private Long checkSectionId(String str) {
        SchoolSection sectionByName = sectionMapper.getSectionByName(str);
        if (null == sectionByName) {
            throw new BizException(ErrorCodeEnum.PUB10000010);
        }
        return sectionByName.getId();
    }


    @Override
    public List<SchoolTeacher> getCharge() {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 0).eq("type", 5);
        return teacherMapper.selectList(queryWrapper);
    }

    @Override
    public SchoolTeacher getTeacherBySection(Long sectionId) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id", sectionId);
        return teacherMapper.selectOne(queryWrapper);

    }

    @Override
    public void updateSectionTeacher(Long sectionId, Long tId) {
        // 1：先置空老的关联ID
        teacherMapper.updateBySectionId(sectionId);
        // 2: 关联新的sectionId
        teacherMapper.updateNewSectionId(sectionId, tId);
    }

    @Override
    public void addSectionId(Long sectionId, Long tId) {
        teacherMapper.updateNewSectionId(sectionId, tId);
    }

    @Override
    public SchoolTeacher getTeacher(Long tId) {
        return teacherMapper.selectById(tId);
    }

    @Override
    public List<SchoolTeacher> getTeachersToClass(Integer type) {
        if (1 == type) {
            QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", 3).or().eq("type", 4);
            return teacherMapper.selectList(queryWrapper);
        } else {
            QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", 1).or().eq("type", 4);
            return teacherMapper.selectList(queryWrapper);
        }
    }

    @Override
    public List<SchoolTeacher> getBuildingTeachers() {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 2);
        return teacherMapper.selectList(queryWrapper);
    }

    @Override
    public Wrapper listRoles(LoginAuthDto loginAuthDto) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 0).or().eq("state", 1);
        List<SysRole> sysRoles = roleMapper.selectList(queryWrapper);
        if (PublicUtil.isNotEmpty(sysRoles)) {
            List<SysRoleVo> vos = new ArrayList<>();
            sysRoles.forEach(s -> {
                SysRoleVo map = new ModelMapper().map(s, SysRoleVo.class);
                vos.add(map);
            });
            return WrapMapper.ok(vos);
        }
        return null;
    }

    @Override
    public Wrapper setRole(LoginAuthDto loginAuthDto, SetRoleDto setRoleDto) {
        if (PublicUtil.isNotEmpty(setRoleDto)) {
            SchoolTeacher teacher = teacherMapper.selectById(setRoleDto.getTeacherId());
            setRoleDto.getRoleId().forEach(i -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setId(gobalInterface.generateId());
                userRole.setUserId(teacher.getAdminId());
                userRole.setRoleId(i);
                userRoleMapper.insert(userRole);
            });
            return WrapMapper.ok("保存成功");
        }
        return null;
    }

    @Override
    public Wrapper importTeacherPictureConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
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
                        String phone = zipEntryName.substring(str3.length() + 1)
                                .replace(".jpg", "")
                                .replace(".png", "")
                                .replace(".JPG", "")
                                .replace(".PNG", "");
                        System.out.println("phone = " + phone);
                        if ("".equals(name) || "".equals(phone)) {
                            throw new BizException(ErrorCodeEnum.PUB10000019);
                        }
                        QueryWrapper<SchoolTeacher> teacherQueryWrapper = new QueryWrapper<>();
                        teacherQueryWrapper.eq("t_name", name).eq("phone", phone);
                        SchoolTeacher teacher = teacherMapper.selectOne(teacherQueryWrapper);
                        if (PublicUtil.isEmpty(teacher)) {
                            throw new BizException(ErrorCodeEnum.PUB10000020);
                        }
                        InputStream inputStream = zip.getInputStream(entry);
                        MultipartFile multipartFile = new MockMultipartFile(str2, str2,
                                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                        SysFileVo sysFileVo = sysFileService.fileUpload(multipartFile);
                        teacher.setImgId(sysFileVo.getId());
                        saveOrUpdate(teacher);
                    }
                }
                return WrapMapper.ok();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return WrapMapper.error("暂时只支持zip压缩包");
        }
        return null;
    }
}
