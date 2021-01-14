package com.safe.campus.service.Impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.*;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.config.ToDevicesUrlConfig;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.MqSysDto;
import com.safe.campus.model.vo.DeviceFaceVO;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherExcelDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.model.vo.SchoolTeacherSectionVo;
import com.safe.campus.model.vo.SchoolTeacherVo;
import com.safe.campus.model.vo.SysFileVo;
import com.safe.campus.model.vo.SysRoleVo;
import com.safe.campus.service.MqMessageService;
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
    private SysAdminUserMapper userMapper;

    @Autowired
    private MqMessageService mqMessageService;


    @Override
    public Wrapper saveTeacherInfo(TeacherInfoDto teacherInfoDto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isEmpty(teacherInfoDto)) {
            return WrapMapper.error("信息不能为空");
        }
        if (PublicUtil.isNotEmpty(teacherMapper.selectOne(new QueryWrapper<SchoolTeacher>().eq("id_number", teacherInfoDto.getIdNumber())))) {
            return WrapMapper.error("不可重复添加");
        }
        SchoolTeacher teacher = new SchoolTeacher();
        teacher.setMasterId(teacherInfoDto.getMasterId());
        teacher.setId(gobalInterface.generateId());
        teacher.setCreatedTime(new Date());
        teacher.setTName(teacherInfoDto.getName());
        teacher.setImgId(teacherInfoDto.getImgId());
        teacher.setIdNumber(teacherInfoDto.getIdNumber());
        teacher.setIsDelete(0);
        teacher.setPhone(teacherInfoDto.getPhone());
        teacher.setJoinTime(teacherInfoDto.getJoinTime());
        teacher.setPosition(teacherInfoDto.getPosition());
        teacher.setSectionId(teacherInfoDto.getSectionId());
        if (null != teacher.getSex()) {
            teacher.setSex(teacherInfoDto.getSex());
        } else {
            String substring = teacher.getIdNumber().substring(16, 17);
            int b = Integer.parseInt(substring);
            if (b % 2 == 0) {
                teacher.setSex(0);
            } else {
                teacher.setSex(1);
            }
        }
        teacher.setState(0);
        teacher.setTNumber(teacherInfoDto.getTNumber());
        teacher.setCreatedUser(loginAuthDto.getUserId());
        teacherMapper.insert(teacher);
        if (teacher.getImgId() != null) {
            // 添加到device那边
            DeviceFaceVO deviceFace = new DeviceFaceVO();
            deviceFace.setImgPath(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            deviceFace.setSchoolId(teacher.getMasterId().toString());
            deviceFace.setUserId(teacher.getId().toString());
            deviceFace.setUserName(teacher.getTName());
            deviceFace.setUserType("T");
            String save = HttpUtils.DO_POST(ToDevicesUrlConfig.ADD_TO_DEVICE, JSON.toJSONString(deviceFace), null, null);
            logger.info("图片添加到设备成功 {}", save);
        }
        MqSysDto mqSysDto = new MqSysDto();
        mqSysDto.setUserId(teacher.getId());
        mqSysDto.setMasterId(teacher.getMasterId());
        mqSysDto.setIdNumber(teacher.getIdNumber());
        mqSysDto.setName(teacher.getTName());
        mqSysDto.setType(1);
        Object MqMsg = mqMessageService.sendSynchronizeMessages("people.insert",new Gson().toJson(mqSysDto));
        logger.info("消息队列 T MqMsg {}",MqMsg);
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
                    if (1 == adminUserByTId.getState()) {
                        vo.setState(-1);
                    } else if (0 == adminUserByTId.getState()) {
                        vo.setState(1);
                    }
                    // 此教职工下面关联的账号
                    QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                    userRoleQueryWrapper.eq("user_id", adminUserByTId.getId());
                    List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
                    if (PublicUtil.isNotEmpty(userRoles)) {
                        List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                        List<SysRole> roles = roleMapper.selectBatchIds(ids);
                        // 此账号下面关联的多个角色
                        List<SchoolTeacherVo.RoleInfos> roleInfos = new ArrayList<>();
                        roles.forEach(r -> {
                            SchoolTeacherVo.RoleInfos roleInfo = new SchoolTeacherVo.RoleInfos();
                            roleInfo.setRoleId(r.getId());
                            roleInfo.setRoleName(r.getRoleName());
                            roleInfos.add(roleInfo);
                        });
                        vo.setRoleInfos(roleInfos);
                    }
                } else {
                    vo.setState(0);
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
        // 删除device那边
        String url = ToDevicesUrlConfig.DELETE_TO_DEVICE + "?schoolId=" + teacherMapper.selectById(id).getMasterId()
                + "&userId=" + id + "&userType=T";
        String delete = HttpUtils.DO_DELETE(url, null, null);
        logger.info("删除设备照片成功{}", delete);
        SchoolTeacher teacher = teacherMapper.selectById(id);
        MqSysDto mqSysDto = new MqSysDto();
        mqSysDto.setUserId(teacher.getId());
        mqSysDto.setMasterId(teacher.getMasterId());
        mqSysDto.setIdNumber(teacher.getIdNumber());
        mqSysDto.setName(teacher.getTName());
        mqSysDto.setType(1);
        Object MqMsg = mqMessageService.sendSynchronizeMessages("people.delete", new Gson().toJson(mqSysDto));
        logger.info("消息队列 T MqMsg {}",MqMsg);
        teacherMapper.deleteById(id);
        return WrapMapper.ok("删除成功");
    }

    @Override
    public Wrapper<SchoolTeacherVo> getTeacherInfo(Long id) {
        if (null == id) {
            return WrapMapper.error("参数不能为空");
        }
        SchoolTeacher teacher = teacherMapper.selectById(id);
        if (PublicUtil.isNotEmpty(teacher)) {
            SchoolTeacherVo vo = new ModelMapper().map(teacher, SchoolTeacherVo.class);
            vo.setName(teacher.getTName());
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
                    roles.forEach(r -> {
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
        teacher.setTName(teacherInfoDto.getName());
        if (teacherInfoDto.getImgId() != null) {
            teacher.setImgId(teacherInfoDto.getImgId());
        }
        teacher.setIdNumber(teacherInfoDto.getIdNumber());
        teacher.setPhone(teacherInfoDto.getPhone());
        teacher.setJoinTime(teacherInfoDto.getJoinTime());
        teacher.setPosition(teacherInfoDto.getPosition());
        teacher.setSectionId(teacherInfoDto.getSectionId());
        teacher.setSex(teacherInfoDto.getSex());
        teacher.setTNumber(teacherInfoDto.getTNumber());
        updateById(teacher);
        // 更新到device那边
        if (null != teacher.getImgId()) {
            DeviceFaceVO deviceFace = new DeviceFaceVO();
            deviceFace.setImgPath(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            deviceFace.setSchoolId(teacher.getMasterId().toString());
            deviceFace.setUserId(teacher.getId().toString());
            deviceFace.setUserName(teacher.getTName());
            deviceFace.setUserType("T");
            String update = HttpUtils.DO_POST(ToDevicesUrlConfig.ADD_TO_DEVICE, JSON.toJSONString(deviceFace), null, null);
            logger.info("更新设备照片成功 {}", update);
        }
        return WrapMapper.ok("修改成功");
    }

    @Override
    public PageWrapper<List<SchoolTeacherVo>> searchTeacherInfo(Long masterId, String context, BaseQueryDto baseQueryDto) {
        QueryWrapper<SchoolTeacher> queryWrapper = new QueryWrapper<>();
        if (null != context && !"".equals(context)) {
            queryWrapper.eq("master_id", masterId)
                    .like("t_name", context).or()
                    .like("t_number", context).or()
                    .like("phone", context).or()
                    .like("join_time", context)
                    .orderByDesc("created_time");
        } else {
            queryWrapper.eq("master_id", masterId).orderByDesc("created_time ");
        }
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
                        roles.forEach(r -> {
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
    public Wrapper importTeacherConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
        SysAdmin sysAdmin = userMapper.selectById(loginAuthDto.getUserId());
        Long masterId = sysAdmin.getMasterId();
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
        if (PublicUtil.isNotEmpty(list)) {
            // 自检idNumber
            Map<String, Long> collect = list.parallelStream().collect(Collectors.groupingBy(TeacherExcelDto::getIdNumber, Collectors.counting()));
            List<String> result = collect.entrySet().stream()
                    .filter(e -> e.getValue() > 1).map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (null != result && !result.isEmpty()) {
                List<TeacherExcelDto> same = list.parallelStream().filter(e -> result.contains(e.getIdNumber())).collect(Collectors.toList());
                return WrapMapper.wrap(400, "身份证号码有重复", same);
            }
            // 检查数据库
            QueryWrapper<SchoolTeacher> teacherQueryWrapper = new QueryWrapper<>();
            teacherQueryWrapper.eq("master_id", masterId);
            List<SchoolTeacher> teachers = teacherMapper.selectList(teacherQueryWrapper);
            Map<String, Long> maps = teachers.parallelStream().collect(Collectors.toMap(SchoolTeacher::getIdNumber, SchoolTeacher::getId));
            for (String idNum : result) {
                if (PublicUtil.isNotEmpty(maps.get(idNum).toString())) {
                    return WrapMapper.wrap(400, "已有此教师", idNum);
                }
            }
            for (TeacherExcelDto t : list) {
                Long id = maps.get(t.getIdNumber());
                if (null == id) {
                    SchoolTeacher teacher = new SchoolTeacher();
                    teacher.setId(gobalInterface.generateId());
                    teacher.setMasterId(masterId);
                    teacher.setTName(t.getName());
                    teacher.setIdNumber(t.getIdNumber());
                    teacher.setPhone(Long.valueOf(t.getPhone()).longValue());
                    teacher.setCreatedTime(new Date());
                    teacher.setState(0);
                    teacher.setIsDelete(0);
                    teacher.setCreatedUser(loginAuthDto.getUserId());
                    // 检查部门
                    if (null != t.getSectionName()) {
                        Long sectionId = checkSectionId(t.getSectionName(), masterId);
                        if (null == sectionId) {
                            return WrapMapper.wrap(400, "部门不存在", t.getSectionName());
                        }
                        teacher.setSectionId(sectionId);
                    }
                    // 去判断非空字段
                    if (null != t.getSex()) {
                        teacher.setSex(checkSex(t.getSex()));
                    } else {
                        String substring = t.getIdNumber().substring(16, 17);
                        int b = Integer.parseInt(substring);
                        if (b % 2 == 0) {
                            teacher.setSex(0);
                        } else {
                            teacher.setSex(1);
                        }
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
                    MqSysDto mqSysDto = new MqSysDto();
                    mqSysDto.setUserId(teacher.getId());
                    mqSysDto.setMasterId(teacher.getMasterId());
                    mqSysDto.setIdNumber(teacher.getIdNumber());
                    mqSysDto.setName(teacher.getTName());
                    mqSysDto.setType(1);
                    Object MqMsg = mqMessageService.sendSynchronizeMessages("people.insert", new Gson().toJson(mqSysDto));
                    logger.info("消息队列 T MqMsg {}",MqMsg);
                    teacherMapper.insert(teacher);
                }
            }
            return WrapMapper.ok("导入成功");
        }
        return WrapMapper.error("Excel导入失败");
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
    private Long checkSectionId(String str, Long masterId) {
        SchoolSection sectionByName = sectionMapper.getSectionByName(str, masterId);
        if (null != sectionByName) {
            return sectionByName.getId();
        }
        return null;
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
        return WrapMapper.error("暂无角色");
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
            userRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", adminUserByTId.getId()));
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
    public Wrapper importTeacherPictureConcentrator(MultipartFile file, LoginAuthDto loginAuthDto) {
        SysAdmin sysAdmin = userMapper.selectById(loginAuthDto.getUserId());
        Long masterId = sysAdmin.getMasterId();
        if (file.isEmpty()) {
            logger.info("上传文件为空");
            throw new BizException(ErrorCodeEnum.PUB10000006);
        }
        String packageName = file.getOriginalFilename();
        if (packageName.matches(".*\\.zip")) {                //是zip压缩文件
            List<String> list = new ArrayList<>();
            File toFile = null;
            try {
                toFile = FileUtils.multipartFileToFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ZipFile zip = new ZipFile(toFile, Charset.forName("GBK"));
                for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String zipEntryName = entry.getName();
                    if (zipEntryName.contains("-")) {
                        logger.warn("zipEntryName---------------------------> {}", zipEntryName);
                        // 截取zip目录之后的字符串
                        String str1 = zipEntryName.substring(0, zipEntryName.indexOf("/"));
                        String str2 = zipEntryName.substring(str1.length() + 1);
                        String name = str2.substring(0, str2.indexOf("-"));
                        logger.warn("name {}", name);
                        String str3 = str2.substring(0, zipEntryName.indexOf("-"));
                        String phone = zipEntryName.substring(str3.length() + 1)
                                .replace(".jpg", "")
                                .replace(".png", "")
                                .replace(".JPG", "")
                                .replace(".PNG", "");
                        logger.warn("phone {}", phone);
                        QueryWrapper<SchoolTeacher> teacherQueryWrapper = new QueryWrapper<>();
                        teacherQueryWrapper.eq("t_name", name).eq("phone", phone).eq("master_id", masterId);
                        SchoolTeacher teacher = teacherMapper.selectOne(teacherQueryWrapper);
                        if (PublicUtil.isEmpty(teacher)) {
                            return WrapMapper.wrap(400, "教师不存在", teacher);
                        }
                        InputStream inputStream = zip.getInputStream(entry);
                        MultipartFile multipartFile = new MockMultipartFile(str2, str2,
                                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                        SysFileVo sysFileVo = sysFileService.fileUpload(multipartFile);
                        teacher.setImgId(sysFileVo.getId());
                        saveOrUpdate(teacher);
                        // 添加到device那边
                        DeviceFaceVO deviceFace = new DeviceFaceVO();
                        deviceFace.setImgPath(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                        deviceFace.setSchoolId(teacher.getMasterId().toString());
                        deviceFace.setUserId(teacher.getId().toString());
                        deviceFace.setUserName(teacher.getTName());
                        deviceFace.setUserType("T");
                        System.out.println("JSON.toJSONString(deviceFace) = " + JSON.toJSONString(deviceFace));
                        String save = HttpUtils.DO_POST(ToDevicesUrlConfig.ADD_TO_DEVICE, JSON.toJSONString(deviceFace), null, null);
                        logger.info("图片添加到设备成功 {}", save);
                    } else {
                        list.add(zipEntryName);
                    }
                }
                FileUtils.delteTempFile(toFile);
                logger.error(list.toString() + "大小是 " + list.size());
                if (list.size() > 1) {
                    List<String> returnList = new ArrayList<>();
                    for (int i = 1; i < list.size(); i++) {
                        String s = list.get(0);
                        String replace = list.get(i).replace(s, "");
                        returnList.add(replace);
                    }
                    return WrapMapper.wrap(400, "其余更新成功,以下照片命名不合法", returnList);
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
                    sysAdmin.setTId(teacher.getId());
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

    @Override
    public Wrapper<List<SchoolTeacherSectionVo>> getSection(Long masterId, Long sectionId) {
        QueryWrapper<SchoolSection> schoolSectionQueryWrapper = new QueryWrapper<>();
        if (null == sectionId) {
            schoolSectionQueryWrapper.eq("master_id", masterId).eq("p_id", 0);
        } else {
            schoolSectionQueryWrapper.eq("p_id", sectionId);
        }
        List<SchoolSection> sections = sectionMapper.selectList(schoolSectionQueryWrapper);
        if (PublicUtil.isNotEmpty(sections)) {
            List<SchoolTeacherSectionVo> list = new ArrayList<>();
            sections.forEach(s -> {
                SchoolTeacherSectionVo vo = new SchoolTeacherSectionVo();
                vo.setSectionId(s.getId());
                vo.setSectionName(s.getSectionName());
                list.add(vo);
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无数据");
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
