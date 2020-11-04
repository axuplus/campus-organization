package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safe.campus.about.utils.HttpUtils;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.*;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.SysFileService;
import com.safe.campus.service.ToOthersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToOthersServiceImpl implements ToOthersService {

    public static final String GET_STATE = "http://47.111.229.199:8890/info/getStateByUserId";


    @Autowired
    private SchoolMasterMapper schoolMaster;

    @Autowired
    private SchoolClassMapper classMapper;

    @Autowired
    private SchoolClassInfoMapper classInfoMapper;

    @Autowired
    private SchoolStudentMapper studentMapper;

    @Autowired
    private SchoolTeacherMapper teacherMapper;

    @Autowired
    private SysAdminUserMapper adminUserMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SchoolSectionMapper sectionMapper;

    @Autowired
    private BuildingStudentMapper buildingStudentMapper;

    @Autowired
    private BuildingNoMapper noMapper;

    @Autowired
    private BuildingLevelMapper levelMapper;

    @Autowired
    private BuildingRoomMapper roomMapper;

    @Autowired
    private BuildingBedMapper bedMapper;

    @Override
    public Wrapper<Map<Long, String>> getAllMasters() {
        List<SchoolMaster> schoolMater = schoolMaster.getSchoolMater();
        if (PublicUtil.isNotEmpty(schoolMater)) {
            return WrapMapper.ok(schoolMater.stream().collect(Collectors.toMap(SchoolMaster::getId, SchoolMaster::getAreaName)));
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<Map<Long, String>> getAllClassesByMasterId(Long masterId) {
        List<SchoolClass> classes = classMapper.selectList(new QueryWrapper<SchoolClass>().eq("master_id", masterId));
        if (PublicUtil.isNotEmpty(classes)) {
            return WrapMapper.ok(classes.stream().collect(Collectors.toMap(SchoolClass::getId, SchoolClass::getClassName)));
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public OthersDto getIdNumberByUserId(String type, Long userId) {
        if (null != type && null != userId) {
            // S, T, P, G,
            if ("S".equals(type)) {
                SchoolStudent student = studentMapper.selectById(userId);
                if (PublicUtil.isNotEmpty(student)) {
                    OthersDto map = new OthersDto();
                    map.setId(userId);
                    map.setState(2);
                    map.setIdNumber(student.getIdNumber());
                    return map;
                }
            } else if ("T".equals(type)) {
                SchoolTeacher teacher = teacherMapper.selectById(userId);
                if (PublicUtil.isNotEmpty(teacher)) {
                    OthersDto map = new OthersDto();
                    map.setId(userId);
                    map.setState(1);
                    map.setIdNumber(teacher.getIdNumber());
                    map.setPhone(teacher.getPhone());
                    map.setTNumber(teacher.getTNumber());
                    return map;
                }
            }
        }
        return null;
    }

    @Override
    public List<SelectStudentListDto> selectStudentList(Map map) {
        if (PublicUtil.isNotEmpty(map)) {
            List<Long> ids = (List<Long>) map.keySet().stream().collect(Collectors.toList());
            List<SchoolStudent> students = studentMapper.selectBatchIds(ids);
            if (PublicUtil.isNotEmpty(students)) {
                List<SelectStudentListDto> list = new ArrayList<>();
                students.forEach(s -> {
                    SelectStudentListDto dto = new SelectStudentListDto();
                    dto.setStudentId(s.getId());
                    if (s.getType() != null) {
                        dto.setType(s.getType());
                    }
                    if (s.getClassId() != null) {
                        dto.setClassId(s.getClassId());
                        dto.setClassName(classMapper.selectById(s.getClassId()).getClassName());
                    }
                    if (s.getClassInfoId() != null) {
                        dto.setClassInfoId(s.getClassInfoId());
                        dto.setClassInfoName(classInfoMapper.selectById(s.getClassInfoId()).getClassInfoName());
                    }
                    list.add(dto);
                });
                return list;
            }
        }
        return null;
    }

    @Override
    public String getTeacherRoles(Long teacherId) {
        List<String> list = new ArrayList<>();
        SysAdmin admin = adminUserMapper.selectOne(new QueryWrapper<SysAdmin>().eq("t_id", teacherId));
        if (PublicUtil.isEmpty(admin)) {
            return null;
        }
        List<String> roleNames = userRoleMapper.getRoleNamesByUserId(admin.getId());
        if (PublicUtil.isEmpty(roleNames)) {
            return null;
        }
        String str = null;
        for (String roleName : roleNames) {
            // "G:访客,T:老师,P:家长,H:班主任,S:保安,D:宿管"
            if ("班主任".equals(roleName)) {
                str = "H";
            } else if ("宿管".equals(roleName)) {
                str = "D";
            } else if ("保安".equals(roleName)) {
                str = "S";
            }
        }
        return str;
    }

    @Override
    public OthersStudentVo getStudentByIdNumber(String idNumber) {
        SchoolStudent student = studentMapper.selectOne(new QueryWrapper<SchoolStudent>().eq("id_number", idNumber));
        if (PublicUtil.isNotEmpty(student)) {
            OthersStudentVo studentVo = new OthersStudentVo();
            studentVo.setStudentId(student.getId());
            studentVo.setStudentName(student.getSName());
            if (null != student.getSNumber()) {
                studentVo.setSNumber(student.getSNumber());
            }
            if (null != student.getImgId()) {
                studentVo.setImg(sysFileService.getFileById(student.getImgId()).getFileUrl());
            }
            if (null != student.getType()) {
                studentVo.setType(student.getType());
            }
            if (null != student.getClassId()) {
                studentVo.setClassName(classMapper.selectById(student.getClassId()).getClassName());
                studentVo.setClassId(student.getClassId());
            }
            if (null != student.getClassInfoId()) {
                studentVo.setClassInfoName(classInfoMapper.selectById(student.getClassInfoId()).getClassInfoName());
            }
            studentVo.setSchoolName(schoolMaster.selectById(student.getMasterId()).getAreaName());
            studentVo.setMasterId(student.getMasterId());
            return studentVo;
        }
        return null;
    }

    @Override
    public Map getStudentTeacherByIdNumber(String idNumber) {
        SchoolStudent student = studentMapper.selectOne(new QueryWrapper<SchoolStudent>().eq("id_number", idNumber));
        if (PublicUtil.isNotEmpty(student)) {
            if (null != student.getClassInfoId()) {
                SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(student.getClassInfoId());
                if (PublicUtil.isNotEmpty(schoolClassInfo)) {
                    if (null != schoolClassInfo.getTId()) {
                        SchoolTeacher teacher = teacherMapper.selectById(schoolClassInfo.getTId());
                        Map<Long, String> map = new HashMap<>();
                        map.put(teacher.getId(), teacher.getTName());
                        return map;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Wrapper<FaceImgInfoVo> getFaceImgInfo(String type, Long id) {
        FaceImgInfoVo infoVo = new FaceImgInfoVo();
        if ("S".equals(type)) {
            infoVo.setState(1);
            FaceImgInfoVo.StudentInfo studentInfo = new FaceImgInfoVo.StudentInfo();
            SchoolStudent student = studentMapper.selectById(id);
            studentInfo.setId(student.getId());
            studentInfo.setStudentName(student.getSName());
            if (null != student.getType()) {
                studentInfo.setType(student.getType());
            }
            if (null != student.getClassId()) {
                studentInfo.setClassName(classMapper.selectById(student.getClassId()).getClassName());
            }
            if (null != student.getClassInfoId()) {
                studentInfo.setClassInfoName(classInfoMapper.selectById(student.getClassInfoId()).getClassInfoName());
            }
            if (null != student.getImgId()) {
                studentInfo.setImg(sysFileService.getFileById(student.getImgId()).getFileUrl());
            }
            infoVo.setStudentInfo(studentInfo);
            return WrapMapper.ok(infoVo);
        } else if ("T".equals(type)) {
            infoVo.setState(2);
            FaceImgInfoVo.TeacherInfo teacherInfo = new FaceImgInfoVo.TeacherInfo();
            SchoolTeacher teacher = teacherMapper.selectById(id);
            teacherInfo.setId(teacher.getId());
            if (null != teacher.getTNumber()) {
                teacherInfo.setTNumber(teacher.getTNumber());
            }
            teacherInfo.setTeacherName(teacher.getTName());
            teacherInfo.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
            if (teacher.getImgId() != null) {
                teacherInfo.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            }
            infoVo.setTeacherInfo(teacherInfo);
            return WrapMapper.ok(infoVo);
        }
        return null;
    }

    @Override
    public OthersTeacherVo getTeacherForMiniApp(String idNumber) {
        SchoolTeacher teacher = teacherMapper.selectOne(new QueryWrapper<SchoolTeacher>().eq("id_number", idNumber));
        if (PublicUtil.isNotEmpty(teacher)) {
            OthersTeacherVo teacherVo = new OthersTeacherVo();
            teacherVo.setMasterId(teacher.getMasterId());
            teacherVo.setTeacherId(teacher.getId());
            if (null != teacher.getImgId()) {
                teacherVo.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
            }
            return teacherVo;
        }
        return null;
    }

    @Override
    public TeacherByPhoneDto getTeacherByPhone(String teacherName, String phone) {
        if (null != teacherName && null != phone) {
            SchoolTeacher teacher = teacherMapper.selectOne(new QueryWrapper<SchoolTeacher>().eq("t_name", teacherName).eq("phone", phone));
            if (PublicUtil.isNotEmpty(teacher)) {
                TeacherByPhoneDto dto = new TeacherByPhoneDto();
                dto.setMasterId(teacher.getMasterId());
                dto.setMasterName(schoolMaster.selectById(teacher.getMasterId()).getAreaName());
                dto.setTeacherId(teacher.getId());
                dto.setTIdNumber(teacher.getIdNumber());
                if (null != teacher.getSectionId()) {
                    dto.setSectionId(teacher.getSectionId());
                    dto.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                }
                if (null != teacher.getImgId()) {
                    dto.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                }
                if (teacher.getSex() != null) {
                    dto.setSex(teacher.getSex());
                }
                return dto;
            }
        }
        return null;
    }

    @Override
    public STDto getStudentAndTeacherInfo(Integer type, Long id) {
        if (null != type && null != id) {
            STDto dto = new STDto();
            if (0 == type) {
                STDto.SInfo sInfo = new STDto.SInfo();
                SchoolStudent student = studentMapper.selectById(id);
                if (PublicUtil.isNotEmpty(student)) {
                    sInfo.setSId(student.getId());
                    sInfo.setType(student.getType());
                    if (null != student.getSNumber()) {
                        sInfo.setSNumber(student.getSNumber());
                    }
                    if (null != student.getClassId()) {
                        sInfo.setClassName(classMapper.selectById(student.getClassId()).getClassName());
                    }
                    if (null != student.getClassInfoId()) {
                        sInfo.setClassName(classInfoMapper.selectById(student.getClassInfoId()).getClassInfoName());
                    }
                    if (null != student.getIdNumber()) {
                        sInfo.setSIdNumber(student.getIdNumber());
                    }
                    BuildingStudent buildingInfo = buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("student_id", id));
                    if (PublicUtil.isNotEmpty(buildingInfo)) {
                        BuildingStudentListDto byIds = noMapper.checkBuildingInfoByIds(buildingInfo.getNoId(), buildingInfo.getLevelId(), buildingInfo.getRoomId(), buildingInfo.getBedId());
                        if (PublicUtil.isNotEmpty(byIds)) {
                            sInfo.setBuildingNo(byIds.getBuildingNo());
                            sInfo.setBuildingLevel(byIds.getBuildingLevel());
                            sInfo.setBuildingRoom(byIds.getBuildingRoom());
                            sInfo.setBuildingBed(byIds.getBedName());
                        }
                    }
                    dto.setSInfo(sInfo);
                    dto.setSchoolId(student.getMasterId());
                    return dto;
                }
            } else {
                STDto.TInfo tInfo = new STDto.TInfo();
                SchoolTeacher teacher = teacherMapper.selectById(id);
                if (PublicUtil.isNotEmpty(teacher)) {
                    tInfo.setTId(teacher.getId());
                    tInfo.setTNumber(teacher.getTNumber());
                    tInfo.setIdNumber(teacher.getIdNumber());
                    if (null != teacher.getPhone()) {
                        tInfo.setPhone(teacher.getPhone());
                    }
                    if (null != teacher.getSectionId()) {
                        tInfo.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                    }
                    if (null != teacher.getPosition()) {
                        tInfo.setPosition(teacher.getPosition());
                    }
                }
                dto.setTInfo(tInfo);
                dto.setSchoolId(teacher.getMasterId());
                return dto;
            }
        }
        return null;
    }

    @Override
    public Wrapper<Map> getBuildingInfoList(Long masterId, Integer type, Long id) {
        if (null != masterId) {
            if (null == type || 1 == type) {
                List<BuildingNo> list = noMapper.selectList(new QueryWrapper<BuildingNo>().eq("master_id", masterId));
                if (PublicUtil.isNotEmpty(list)) {
                    return WrapMapper.ok(list.stream().collect(Collectors.toMap(BuildingNo::getId, BuildingNo::getBuildingNo)));
                }
            } else if (2 == type && null != id) {
                List<BuildingLevel> list = levelMapper.selectList(new QueryWrapper<BuildingLevel>().eq("building_no_id", id));
                if (PublicUtil.isNotEmpty(list)) {
                    return WrapMapper.ok(list.stream().collect(Collectors.toMap(BuildingLevel::getId, BuildingLevel::getBuildingLevel)));
                }
            } else if (3 == type && null != id) {
                List<BuildingRoom> list = roomMapper.selectList(new QueryWrapper<BuildingRoom>().eq("building_level_id", id));
                if (PublicUtil.isNotEmpty(list)) {
                    return WrapMapper.ok(list.stream().collect(Collectors.toMap(BuildingRoom::getId, BuildingRoom::getBuildingRoom)));
                }
            }
        }
        return null;
    }

    @Override
    public PageWrapper<List<BuildingInfoListRoomVo>> getBuildingInfoListByIds(Long masterId, Integer type, Long id, BaseQueryDto baseQueryDto) {
        if (null != masterId) {
            List<BuildingRoomDto> rooms = null;
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            if (0 == type) {
                rooms = roomMapper.selectByNothing(masterId);
            } else if (1 == type) {
                rooms = roomMapper.selectByNoId(id);
            } else if (2 == type) {
                rooms = roomMapper.selectByLevelId(id);
            } else if (3 == type) {
                rooms = roomMapper.selectByRoomId(id);
            }
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(rooms)) {
                List<BuildingInfoListRoomVo> list = new ArrayList<>();
                rooms.forEach(room -> {
                    BuildingInfoListRoomVo vo = new BuildingInfoListRoomVo();
                    vo.setRoomId(room.getRoomId());
                    vo.setRoomName(room.getRoomName());
                    vo.setBuildingNo(room.getNoName());
                    vo.setBuildingLevel(room.getLevelName());
                    vo.setBedCount(bedMapper.selectCount(new QueryWrapper<BuildingBed>().eq("room_id", room.getRoomId())));
                    vo.setStudentCount(buildingStudentMapper.selectCount(new QueryWrapper<BuildingStudent>().eq("room_id", room.getRoomId())));
                    list.add(vo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public Wrapper<BuildingInfoListBedVo> getStudentsByRoom(Long roomId) {
        if (null != roomId) {
            BuildingInfoListBedVo vo = new BuildingInfoListBedVo();
            List<BuildingRoomDto> dtos = roomMapper.selectByRoomId(roomId);
            BuildingRoomDto dto = dtos.get(0);
            vo.setRoomId(roomId);
            vo.setCurrentBuildingInfo(dto.getNoName() + dto.getLevelName() + dto.getRoomName());
            List<BuildingBed> beds = bedMapper.selectList(new QueryWrapper<BuildingBed>().eq("room_id", roomId));
            if (PublicUtil.isNotEmpty(beds)) {
                List<BuildingInfoListBedVo.Bed> vos = new ArrayList<>();
                beds.forEach(b -> {
                    BuildingInfoListBedVo.Bed bed = new BuildingInfoListBedVo.Bed();
                    bed.setBedName(b.getBedName());
                    BuildingStudent studentBed = buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("bed_id", b.getId()));
                    if (PublicUtil.isNotEmpty(studentBed)) {
                        SchoolStudent student = studentMapper.selectById(studentBed.getStudentId());
                        bed.setSName(student.getSName());
                        bed.setSNumber(student.getSNumber());
                        if (null != student.getClassId() && null != student.getClassInfoId()) {
                            bed.setClassInfo(classMapper.selectById(student.getClassId()).getClassName() + "/" + classInfoMapper.selectById(student.getClassInfoId()).getClassInfoName());
                        }
                        if (null != student.getClassInfoId()) {
                            SchoolClassInfo classInfo = classInfoMapper.selectById(student.getClassInfoId());
                            if (null != classInfo.getTId()) {
                                SchoolTeacher teacher = teacherMapper.selectById(classInfo.getTId());
                                bed.setTeacherInfo(teacher.getTName() + "/" + teacher.getPhone());
                            }
                        }
                        String doGet = HttpUtils.DO_GET(GET_STATE + "?userId=" + student.getId(), null, null);
                        System.out.println("doGet = " + doGet);
                        if (null != doGet && !"".equals(doGet)) {
                            Map map = new Gson().fromJson(doGet, new TypeToken<Map<String, String>>() {
                            }.getType());
                            bed.setState(Integer.valueOf(map.get("state").toString()));
                            String time = map.get("time").toString().replaceAll("T", " ").substring(0, 19);
                            bed.setTime(time);
                            bed.setEquipmentName(map.get("equipmentName").toString());
                        }
                    }
                    vos.add(bed);
                });
                vo.setBeds(vos);
                return WrapMapper.ok(vo);
            }
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public List<Object> getStudentsOrTeachersByType(Long masterId, Integer type) {
        List<Object> list = new ArrayList<>();
        if (null != masterId && null != type) {
            if (1 == type) {
                list = Collections.singletonList(studentMapper.selectList(new QueryWrapper<SchoolStudent>().eq("master_id", masterId)));
            } else {
                list = Collections.singletonList(teacherMapper.selectList(new QueryWrapper<SchoolTeacher>().eq("master_id", masterId)));
            }
        }
        return list;
    }

    @Override
    public List<ListStudentByTeacherVo> getStudentByTeacherId(Long teacherId) {
        if (null != teacherId) {
            List<ListStudentByTeacherVo> vos = new ArrayList<>();
            List<SchoolClassInfo> infos = classInfoMapper.selectList(new QueryWrapper<SchoolClassInfo>().eq("t_id", teacherId));
            if (PublicUtil.isNotEmpty(infos)) {
                infos.forEach(info -> {
                    List<SchoolStudent> students = studentMapper.selectList(new QueryWrapper<SchoolStudent>().eq("class_info_id", info.getId()));
                    if (PublicUtil.isNotEmpty(students)) {
                        students.forEach(student -> {
                            ListStudentByTeacherVo vo = new ListStudentByTeacherVo();
                            vo.setSId(student.getId());
                            vo.setSName(student.getSName());
                            if (null != student.getSNumber()) {
                                vo.setSNumber(student.getSNumber());
                            }
                            if (null != student.getType()) {
                                vo.setType(student.getType());
                            }
                            vos.add(vo);
                        });
                    }
                });
            }
            return vos;
        }
        return null;
    }

    @Override
    public StudentCountVo getStudentCountByTeacherPhone(String tName, Long teacherId) {
        if (null != tName && null != teacherId) {
            SchoolTeacher teacher = teacherMapper.selectOne(new QueryWrapper<SchoolTeacher>().eq("t_name", tName).eq("id", teacherId));
            if (PublicUtil.isNotEmpty(teacher)) {
                SchoolClassInfo info = classInfoMapper.selectOne(new QueryWrapper<SchoolClassInfo>().eq("t_id", teacher.getId()));
                if (PublicUtil.isNotEmpty(info)) {
                    StudentCountVo countVo = new StudentCountVo();
                    countVo.setClassId(info.getClassId());
                    countVo.setClassName(classMapper.selectById(info.getClassId()).getClassName());
                    countVo.setClassInfoId(info.getId());
                    countVo.setClassInfoName(info.getClassInfoName());
                    countVo.setLiveCount(studentMapper.selectCount(new QueryWrapper<SchoolStudent>().eq("class_info_id", info.getId()).eq("type", 1)));
                    countVo.setLeaveCount(studentMapper.selectCount(new QueryWrapper<SchoolStudent>().eq("class_info_id", info.getId()).eq("type", 2)));
                    countVo.setTotal(studentMapper.selectCount(new QueryWrapper<SchoolStudent>().eq("class_info_id", info.getId())));
                    return countVo;
                }
            }
        }
        return null;
    }

    @Override
    public PageWrapper<List<AllStudentsVo>> getAllStudents(Long schoolId, BaseQueryDto baseQueryDto) {
        if (null != schoolId) {
            List<AllStudentsVo> list = new ArrayList<>();
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<SchoolStudent> students = studentMapper.selectList(new QueryWrapper<SchoolStudent>().eq("master_id", schoolId));
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(students)) {
                students.forEach(student -> {
                    AllStudentsVo vo = new AllStudentsVo();
                    vo.setStudentId(student.getId());
                    vo.setStudentName(student.getSName());
                    vo.setIdNumber(student.getIdNumber());
                    if (null != student.getType()) {
                        vo.setType(student.getType());
                        if (1 == student.getType()) {
                            AllStudentsVo.BuildingInfo buildingInfo = new AllStudentsVo.BuildingInfo();
                            BuildingStudent buildingStudent = buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("student_id", student.getId()));
                            if (PublicUtil.isNotEmpty(buildingStudent)) {
                                buildingInfo.setNoName(noMapper.selectById(buildingStudent.getNoId()).getBuildingNo());
                                buildingInfo.setLevelName(levelMapper.selectById(buildingStudent.getLevelId()).getBuildingLevel());
                                buildingInfo.setRoomName(roomMapper.selectById(buildingStudent.getRoomId()).getBuildingRoom());
                                buildingInfo.setBedName(bedMapper.selectById(buildingStudent.getBedId()).getBedName());
                                vo.setBuildingInfo(buildingInfo);
                            }
                        }
                    }
                    if (student.getJoinTime() != null) {
                        vo.setJoinTime(student.getJoinTime());
                    }
                    if (null != student.getEndTime()) {
                        vo.setEndTime(student.getEndTime());
                    }
                    if (null != student.getImgId()) {
                        vo.setImg(sysFileService.getFileById(student.getImgId()).getFileUrl());
                    }
                    if (null != student.getSex()) {
                        vo.setSex(student.getSex());
                    }
                    if (null != student.getClassId() && null != student.getClassInfoId()) {
                        AllStudentsVo.ClassInfo classInfo = new AllStudentsVo.ClassInfo();
                        classInfo.setClassId(student.getClassId());
                        classInfo.setClassName(classMapper.selectById(student.getClassId()).getClassName());
                        classInfo.setClassInfoId(student.getClassInfoId());
                        SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(student.getClassInfoId());
                        classInfo.setClassInfoName(schoolClassInfo.getClassInfoName());
                        if (null != schoolClassInfo.getTId()) {
                            SchoolTeacher teacher = teacherMapper.selectById(schoolClassInfo.getTId());
                            classInfo.setTeacherId(teacher.getId());
                            classInfo.setTeacherName(teacher.getTName());
                            classInfo.setTeacherNumber(teacher.getTNumber());
                        }
                        vo.setClassInfo(classInfo);
                    }
                    list.add(vo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public PageWrapper<List<AllTeachersVo>> getAllTeachers(Long schoolId, BaseQueryDto baseQueryDto) {
        if (null != schoolId) {
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<SchoolTeacher> teachers = teacherMapper.selectList(new QueryWrapper<SchoolTeacher>().eq("master_id", schoolId));
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(teachers)) {
                List<AllTeachersVo> list = new ArrayList<>();
                teachers.forEach(teacher -> {
                    AllTeachersVo vo = new AllTeachersVo();
                    vo.setId(teacher.getId());
                    vo.setName(teacher.getTName());
                    vo.setIdNumber(teacher.getIdNumber());
                    if (null != teacher.getImgId()) {
                        vo.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                    }
                    if (null != teacher.getPhone()) {
                        vo.setPhone(teacher.getPhone());
                    }
                    if (null != teacher.getPosition()) {
                        vo.setPosition(teacher.getPosition());
                    }
                    if (null != teacher.getTNumber()) {
                        vo.setTNumber(teacher.getTNumber());
                    }
                    if (null != teacher.getSex()) {
                        vo.setSex(teacher.getSex());
                    }
                    vo.setSectionId(teacher.getSectionId());
                    vo.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                    SchoolClass schoolClass = classMapper.selectOne(new QueryWrapper<SchoolClass>().eq("t_id", teacher.getId()));
                    if (PublicUtil.isNotEmpty(schoolClass)) {
                        vo.setClassId(schoolClass.getId());
                        vo.setClassName(schoolClass.getClassName());
                    }
                    SchoolClassInfo schoolClassInfo = classInfoMapper.selectOne(new QueryWrapper<SchoolClassInfo>().eq("t_id", teacher.getId()));
                    if (PublicUtil.isNotEmpty(schoolClassInfo)) {
                        vo.setClassInfoId(schoolClassInfo.getId());
                        vo.setClassInfoName(schoolClassInfo.getClassInfoName());
                    }
                    list.add(vo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public Wrapper<List<BuildingClassVo>> getAllBuildings(Long schoolId) {
        if (null != schoolId) {
            List<BuildingClassVo> list = new ArrayList<>();
            List<BuildingNo> nos = noMapper.selectList(new QueryWrapper<BuildingNo>().eq("master_id", schoolId));
            if (PublicUtil.isNotEmpty(nos)) {
                nos.forEach(no -> {
                    BuildingClassVo vo = new BuildingClassVo();
                    vo.setId(no.getId());
                    vo.setClassName(no.getBuildingNo());
                    list.add(vo);
                });
                return WrapMapper.ok(list);
            }
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<WhiteListVo> getPersonsByType(Long schoolId, Integer type) {
        if (null != schoolId && null != type) {
            WhiteListVo whiteListVo = new WhiteListVo();
            if (0 == type) {
                whiteListVo.setState(0);
                List<WhiteListVo.StudentInfos> list = new ArrayList<>();
                List<SchoolStudent> students = studentMapper.selectList(new QueryWrapper<SchoolStudent>().eq("master_id", schoolId));
                if (PublicUtil.isNotEmpty(students)) {
                    students.forEach(student -> {
                        WhiteListVo.StudentInfos vo = new WhiteListVo.StudentInfos();
                        vo.setStudentId(student.getId());
                        vo.setStudentName(student.getSName());
                        vo.setIdNumber(student.getIdNumber());
                        if (student.getJoinTime() != null) {
                            vo.setJoinTime(student.getJoinTime());
                        }
                        if (null != student.getEndTime()) {
                            vo.setEndTime(student.getEndTime());
                        }
                        if (null != student.getImgId()) {
                            vo.setImg(sysFileService.getFileById(student.getImgId()).getFileUrl());
                        }
                        if (null != student.getSex()) {
                            vo.setSex(student.getSex());
                        }
                        if (null != student.getType()) {
                            vo.setType(student.getType());
                        }
                        if (null != student.getClassId() && null != student.getClassInfoId()) {
                            vo.setClassId(student.getClassId());
                            vo.setClassName(classMapper.selectById(student.getClassId()).getClassName());
                            vo.setClassInfoId(student.getClassInfoId());
                            SchoolClassInfo schoolClassInfo = classInfoMapper.selectById(student.getClassInfoId());
                            vo.setClassInfoName(schoolClassInfo.getClassInfoName());
                        }
                        list.add(vo);
                    });
                    whiteListVo.setStudentInfos(list);
                }
            } else {
                whiteListVo.setState(1);
                List<SchoolTeacher> teachers = teacherMapper.selectList(new QueryWrapper<SchoolTeacher>().eq("master_id", schoolId));
                if (PublicUtil.isNotEmpty(teachers)) {
                    List<WhiteListVo.TeacherInfos> list = new ArrayList<>();
                    teachers.forEach(teacher -> {
                        WhiteListVo.TeacherInfos vo = new WhiteListVo.TeacherInfos();
                        vo.setId(teacher.getId());
                        vo.setName(teacher.getTName());
                        vo.setIdNumber(teacher.getIdNumber());
                        if (null != teacher.getImgId()) {
                            vo.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
                        }
                        if (null != teacher.getPhone()) {
                            vo.setPhone(teacher.getPhone());
                        }
                        if (null != teacher.getPosition()) {
                            vo.setPosition(teacher.getPosition());
                        }
                        if (null != teacher.getTNumber()) {
                            vo.setTNumber(teacher.getTNumber());
                        }
                        if (null != teacher.getSex()) {
                            vo.setSex(teacher.getSex());
                        }
                        vo.setSectionId(teacher.getSectionId());
                        vo.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                        list.add(vo);
                    });
                    whiteListVo.setTeacherInfos(list);
                }
            }
            return WrapMapper.ok(whiteListVo);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<List<BuildingNoWithLevelVo>> getBuildingNoAndLevels(String schoolId) {
        if (null != schoolId) {
            List<BuildingNo> nos = noMapper.selectList(new QueryWrapper<BuildingNo>().eq("master_id", schoolId));
            if (PublicUtil.isNotEmpty(nos)) {
                List<BuildingNoWithLevelVo> list = new ArrayList<>();
                nos.forEach(no -> {
                    BuildingNoWithLevelVo vo = new BuildingNoWithLevelVo();
                    vo.setId(no.getId());
                    vo.setBuildingNo(no.getBuildingNo());
                    List<BuildingLevel> levels = levelMapper.selectList(new QueryWrapper<BuildingLevel>().eq("building_no_id", no.getId()));
                    if (PublicUtil.isNotEmpty(levels)) {
                        List<BuildingNoWithLevelVo.BuildingLevels> levelsList = new ArrayList<>();
                        levels.forEach(level -> {
                            BuildingNoWithLevelVo.BuildingLevels buildingLevel = new BuildingNoWithLevelVo.BuildingLevels();
                            buildingLevel.setId(level.getId());
                            buildingLevel.setBuildingLevel(level.getBuildingLevel());
                            levelsList.add(buildingLevel);
                        });
                        vo.setBuildingLevels(levelsList);
                    }
                    list.add(vo);
                });
                return WrapMapper.ok(list);
            }
        }
        return WrapMapper.error("暂无数据");
    }
}
