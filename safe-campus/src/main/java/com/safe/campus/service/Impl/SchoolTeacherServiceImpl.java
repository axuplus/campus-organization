package com.safe.campus.service.Impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.*;
import com.safe.campus.about.utils.wrapper.*;
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
import java.text.SimpleDateFormat;
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


    @Override
    public Wrapper saveTeacherInfo(TeacherInfoDto teacherInfoDto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isEmpty(teacherInfoDto)) {
            return WrapMapper.error("信息不能为空");
        }
        SchoolTeacher teacher = new SchoolTeacher();
        teacher.setMasterId(teacherInfoDto.getMasterId());
        teacher.setId(gobalInterface.generateId());
        teacher.setCreatedTime(new Date());
        teacher.setTName(teacherInfoDto.getTName());
        teacher.setImgId(teacherInfoDto.getImgId());
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
        teacherMapper.insert(teacher);
        return WrapMapper.ok("保存成功");
    }


    @Override
    public PageWrapper<List<SchoolTeacherVo>> listTeacherInfo(Integer type, Long masterId, Long id, BaseQueryDto baseQueryDto) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        if (2 == type) {
            queryWrapper.eq("master_id", masterId).eq("section_id", id).orderByDesc("created_time ");
        } else if (1 == type) {
            queryWrapper.eq("master_id", masterId).orderByDesc("created_time ");
        }
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SchoolTeacher> teachers = teacherMapper.selectList(queryWrapper);
        Long total = page.getTotal();
        if (PublicUtil.isNotEmpty(teachers)) {
            List<SchoolTeacherVo> list = new ArrayList<>();
            teachers.forEach(teacher -> {
                SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
                vo.setPhoto(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                // 关联班级
                List<SchoolClassInfo> classInfoByTid = classInfoMapper.getClassInfoByTid(teacher.getId());
                if (PublicUtil.isNotEmpty(classInfoByTid)) {
                    List<String> classList = new ArrayList<>();
                    classInfoByTid.forEach(t -> {
                        SchoolClass schoolClass = classMapper.selectById(t.getClassId());
                        classList.add(schoolClass.getClassName() + " " + t.getClassInfoName());
                    });
                    vo.setClassInformation(classList);
                }
                // 部门
                SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
                if (null != section) {
                    vo.setSectionName(section.getSectionName());
                }
                SysAdmin adminUserByTId = adminUserMapper.getAdminUserByTId(teacher.getId());
                if (PublicUtil.isNotEmpty(adminUserByTId)) {
                    // 此教职工下面关联的账号
                    QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                    userRoleQueryWrapper.eq("user_id", adminUserByTId.getId());
                    List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                    if (PublicUtil.isNotEmpty(userRoles)) {
                        List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                        List<SysRole> roles = roleMapper.selectBatchIds(ids);
                        // 此账号下面关联的多个角色
                        List<SchoolTeacherVo.RoleInfos> roleInfos = new ArrayList<>();
                        roles.forEach(r ->{
                            SchoolTeacherVo.RoleInfos roleInfo = new SchoolTeacherVo.RoleInfos();
                            roleInfo.setRoleId(r.getId());
                            roleInfo.setRoleName(r.getRoleName());
                            roleInfos.add(roleInfo);
                        });
                        vo.setRoleInfos(roleInfos);
                    }
                }
                list.add(vo);
            });
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public Wrapper deleteTeacherInfo(Long id) {
        if (null == id) {
            return WrapMapper.error("参数不能为空");
        }
        // 先删除关联账号
        QueryWrapper<SysAdmin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("t_id", id);
        adminUserMapper.delete(adminQueryWrapper);
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
            List<SchoolClassInfo> classInfoByTid = classInfoMapper.getClassInfoByTid(teacher.getId());
            if (PublicUtil.isNotEmpty(classInfoByTid)) {
                List<String> classList = new ArrayList<>();
                classInfoByTid.forEach(t -> {
                    SchoolClass schoolClass = classMapper.selectById(t.getClassId());
                    classList.add(schoolClass.getClassName() + " " + t.getClassInfoName());
                });
                vo.setClassInformation(classList);
            }
            SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
            if (null != section) {
                vo.setSectionName(section.getSectionName());
            }
            vo.setPhoto(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            SysAdmin adminUserByTId = adminUserMapper.getAdminUserByTId(teacher.getId());
            if (PublicUtil.isNotEmpty(adminUserByTId)) {
                // 此教职工下面关联的账号
                QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_id", adminUserByTId.getId());
                List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                if (PublicUtil.isNotEmpty(userRoles)) {
                    List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> roles = roleMapper.selectBatchIds(ids);
                    // 此账号下面关联的多个角色
                    // vo.setRoleId(roles.stream().map(SysRole::getId).collect(Collectors.toList()));
                    // vo.setRoleName(roles.stream().map(SysRole::getRoleName).collect(Collectors.toList()));
                    List<SchoolTeacherVo.RoleInfos> roleInfos = new ArrayList<>();
                    roles.forEach(r ->{
                        SchoolTeacherVo.RoleInfos roleInfo = new SchoolTeacherVo.RoleInfos();
                        roleInfo.setRoleId(r.getId());
                        roleInfo.setRoleName(r.getRoleName());
                        roleInfos.add(roleInfo);
                    });
                    vo.setRoleInfos(roleInfos);
                }
            }
            return WrapMapper.ok(vo);
        }
        return null;
    }

    @Override
    public Wrapper editTeacherInfo(TeacherInfoDto teacherInfoDto) {
        if (PublicUtil.isEmpty(teacherInfoDto)) {
            return WrapMapper.error("修改参数不能为空");
        }
        SchoolTeacher teacher = teacherMapper.selectById(teacherInfoDto.getId());
        teacher.setTName(teacherInfoDto.getTName());
        teacher.setImgId(teacherInfoDto.getImgId());
        teacher.setIdNumber(teacherInfoDto.getIdNumber());
        teacher.setPhone(teacherInfoDto.getPhone());
        teacher.setJoinTime(teacherInfoDto.getJoinTime());
        teacher.setPosition(teacherInfoDto.getPosition());
        teacher.setSectionId(teacherInfoDto.getSectionId());
        teacher.setSex(teacherInfoDto.getSex());
        teacher.setTNumber(teacherInfoDto.getTNumber());
        updateById(teacher);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public PageWrapper<List<SchoolTeacherVo>> searchTeacherInfo(Long masterId, String context, BaseQueryDto baseQueryDto) {
        if ("".equals(context)) {
            return PageWrapMapper.wrap(200, "搜索条件不能为空");
        }
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId)
                .like("t_name", context).or()
                .like("t_number", context).or()
                .like("phone", context).or()
                .like("join_time", context)
                .orderByDesc("created_time");
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SchoolTeacher> teachers = teacherMapper.selectList(queryWrapper);
        Long total = page.getTotal();
        List<SchoolTeacherVo> list = new ArrayList<>();
        if (PublicUtil.isNotEmpty(teachers)) {
            teachers.forEach(teacher -> {
                SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
                vo.setPhoto(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                // 关联班级
                List<SchoolClassInfo> classInfoByTid = classInfoMapper.getClassInfoByTid(teacher.getId());
                if (PublicUtil.isNotEmpty(classInfoByTid)) {
                    List<String> classList = new ArrayList<>();
                    classInfoByTid.forEach(t -> {
                        SchoolClass schoolClass = classMapper.selectById(t.getClassId());
                        classList.add(schoolClass.getClassName() + " " + t.getClassInfoName());
                    });
                    vo.setClassInformation(classList);
                }
                // 部门
                SchoolSection section = sectionMapper.selectById(teacher.getSectionId());
                if (null != section) {
                    vo.setSectionName(section.getSectionName());
                }
                // 此教职工下面关联的账号
                SysAdmin adminUserByTId = adminUserMapper.getAdminUserByTId(teacher.getId());
                if (PublicUtil.isNotEmpty(adminUserByTId)) {
                    QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                    userRoleQueryWrapper.eq("user_id", adminUserByTId.getId());
                    List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                    if (PublicUtil.isNotEmpty(userRoles)) {
                        List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                        List<SysRole> roles = roleMapper.selectBatchIds(ids);
                        // 此账号下面关联的多个角色
                        List<SchoolTeacherVo.RoleInfos> roleInfos = new ArrayList<>();
                        roles.forEach(r ->{
                            SchoolTeacherVo.RoleInfos roleInfo = new SchoolTeacherVo.RoleInfos();
                            roleInfo.setRoleId(r.getId());
                            roleInfo.setRoleName(r.getRoleName());
                            roleInfos.add(roleInfo);
                        });
                        vo.setRoleInfos(roleInfos);
                    }
                }
                list.add(vo);
            });
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }


    /**
     * excel导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Wrapper importTeacherConcentrator(MultipartFile file, Long masterId, LoginAuthDto loginAuthDto) {
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
            QueryWrapper<SchoolTeacher> teacherQueryWrapper = new QueryWrapper<>();
            teacherQueryWrapper.eq("master_id", masterId);
            List<SchoolTeacher> teachers = teacherMapper.selectList(teacherQueryWrapper);
            Map<String, Long> maps = teachers.stream().collect(Collectors.toMap(SchoolTeacher::getIdNumber, SchoolTeacher::getId));
            list.forEach(t -> {
                Long id = maps.get(t.getIdNumber());
                // 更新
                if (null != id) {
                    SchoolTeacher teacher = teacherMapper.selectById(id);
                    // 检查部门
                    if (null != t.getSectionName()) {
                        teacher.setSectionId(checkSectionId(t.getSectionName()));
                    }
                    // 去判断非空字段
                    if (null != t.getSex()) {
                        teacher.setSex(checkSex(t.getSex()));
                    }
                    if (null != t.getTeacherNumber()) {
                        teacher.setTNumber(t.getTeacherNumber());
                    }
                    if (null != t.getJoinTime()) {
                        String s = EasyExcelUtil.formatExcelDate(Integer.valueOf(t.getJoinTime()));
                        teacher.setJoinTime(s);
                    }
                    if (null != t.getPositionName()) {
                        teacher.setPosition(t.getPositionName());
                    }
                    if (null != t.getShape()) {
                        teacher.setState(getState(t.getShape()));
                    }
                    teacherMapper.updateById(teacher);
                } else {
                    // 插入
                    SchoolTeacher teacher = new SchoolTeacher();
                    teacher.setId(gobalInterface.generateId());
                    teacher.setMasterId(masterId);
                    teacher.setTName(t.getName());
                    teacher.setIdNumber(t.getIdNumber());
                    teacher.setPhone(Long.valueOf(t.getPhone()));
                    teacher.setCreatedTime(new Date());
                    teacher.setIsDelete(0);
                    teacher.setCreatedUser(loginAuthDto.getUserId());
                    // 检查部门
                    if (null != t.getSectionName()) {
                        teacher.setSectionId(checkSectionId(t.getSectionName()));
                    }
                    // 去判断非空字段
                    if (null != t.getSex()) {
                        teacher.setSex(checkSex(t.getSex()));
                    }
                    if (null != t.getTeacherNumber()) {
                        teacher.setTNumber(t.getTeacherNumber());
                    }
                    if (null != t.getJoinTime()) {
                        String s = EasyExcelUtil.formatExcelDate(Integer.valueOf(t.getJoinTime()));
                        teacher.setJoinTime(s);
                    }
                    if (null != t.getPositionName()) {
                        teacher.setPosition(t.getPositionName());
                    }
                    if (null != t.getShape()) {
                        teacher.setState(getState(t.getShape()));
                    }
                    teacherMapper.insert(teacher);
                }
            });
            return WrapMapper.ok("导入成功");
        }
        return WrapMapper.error("位置错误");
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
            throw new BizException(ErrorCodeEnum.PUB10000012);
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

    /**
     *
     */
    private Integer getState(String str) {
        if ("在职".equals(str)) {
            return 0;
        } else if ("离职".equals(str)) {
            return 1;
        } else {
            throw new BizException(ErrorCodeEnum.PUB10000018);
        }
    }


    @Override
    public List<SchoolTeacher> getCharge(Long masterId) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 0).eq("master_id", masterId);
        return teacherMapper.selectList(queryWrapper);
    }

    @Override
    public SchoolTeacher getTeacherBySection(Long id) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return teacherMapper.selectOne(queryWrapper);

    }

    @Override
    public SchoolTeacher getTeacher(Long tId) {
        return teacherMapper.selectById(tId);
    }

    @Override
    public List<SchoolTeacher> getTeachersToClass(Long masterId) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId).eq("state", 0);
        return teacherMapper.selectList(queryWrapper);
    }

    @Override
    public List<SchoolTeacher> getBuildingTeachers(Long masterId) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId);
        return teacherMapper.selectList(queryWrapper);
    }

    @Override
    public Wrapper listRoles(Long masterId, LoginAuthDto loginAuthDto) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId).eq("state", 1);
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
            // 去查admin表有没有启用教职工的账号
            SysAdmin adminUserByTId = adminUserMapper.getAdminUserByTId(teacher.getId());
            if (PublicUtil.isEmpty(adminUserByTId)) {
                return WrapMapper.error("此教工暂无账号,请先启用账户");
            }
            setRoleDto.getRoleId().forEach(i -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setId(gobalInterface.generateId());
                userRole.setUserId(adminUserByTId.getId());
                userRole.setRoleId(i);
                userRoleMapper.insert(userRole);
            });
            return WrapMapper.ok("保存成功");
        }
        return null;
    }

    @Override
    public Wrapper importTeacherPictureConcentrator(MultipartFile file, Long masterId, LoginAuthDto loginAuthDto) {
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
                        teacherQueryWrapper.eq("t_name", name).eq("phone", phone).eq("master_id", masterId);
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

    @Override
    public Wrapper active(LoginAuthDto loginAuthDto, Long id, Long masterId, Integer state) {
        if (null != id && null != state) {
            if (0 == state) {
                // 先查询admin表看其是否存在
                QueryWrapper<SysAdmin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("t_id", id);
                SysAdmin admin = adminUserMapper.selectOne(adminQueryWrapper);
                // 已经存在的状态下就改变状态
                if (PublicUtil.isNotEmpty(admin)) {
                    admin.setState(0);
                    adminUserMapper.updateById(admin);
                    return WrapMapper.ok("操作成功");
                } else {
                    // 未存在的情况下就说明没有 然后新增
                    SysAdmin sysAdmin = new SysAdmin();
                    sysAdmin.setId(gobalInterface.generateId());
                    sysAdmin.setState(0);
                    sysAdmin.setCreateTime(new Date());
                    SchoolTeacher teacher = teacherMapper.selectById(id);
                    sysAdmin.setUserName(teacher.getPhone().toString());
                    sysAdmin.setPassword(Md5Utils.md5Str(teacher.getIdNumber().substring(teacher.getIdNumber().length() - 6)));
                    sysAdmin.setAppKey(StringUtils.getRandomString(7).toUpperCase());
                    sysAdmin.setAppSecret(StringUtils.getRandomString(13).toLowerCase());
                    sysAdmin.setLevel(3);
                    sysAdmin.setType(3);
                    sysAdmin.setMasterId(masterId);
                    sysAdmin.setCreateUser(loginAuthDto.getUserId());
                    adminUserMapper.insert(sysAdmin);
                    return WrapMapper.ok("操作成功");
                }
            } else if (1 == state) {
                // 只有有了账号的情况下有停用
                QueryWrapper<SysAdmin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("t_id", id);
                SysAdmin admin = adminUserMapper.selectOne(adminQueryWrapper);
                admin.setState(1);
                adminUserMapper.updateById(admin);
                return WrapMapper.ok("操作成功");
            }
        }
        return WrapMapper.error("参数不正确");
    }

    @Override
    public List<SchoolTeacher> searchTeachersByName(String context, Long masterId) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId).like("t_name", context);
        return teacherMapper.selectList(queryWrapper);
    }
}


/**
 * // 教职工角色更新
 * SysAdmin userByTId = adminUserMapper.getAdminUserByTId(teacher.getId());
 * if(PublicUtil.isNotEmpty(userByTId)) {
 * userRoleMapper.deleteByAdminId(userByTId.getId());
 * if (PublicUtil.isNotEmpty(teacherInfoDto.getRoleId())) {
 * teacherInfoDto.getRoleId().forEach(i -> {
 * SysUserRole role = new SysUserRole();
 * role.setId(gobalInterface.generateId());
 * role.setRoleId(i);
 * role.setUserId(userByTId.getId());
 * userRoleMapper.insert(role);
 * });
 * }
 * }
 */
