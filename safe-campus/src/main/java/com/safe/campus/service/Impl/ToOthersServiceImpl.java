package com.safe.campus.service.Impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ToOthersServiceImpl implements ToOthersService {

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
            }
            if (null != student.getClassInfoId()) {
                studentVo.setClassInfoName(classInfoMapper.selectById(student.getClassInfoId()).getClassInfoName());
            }
            studentVo.setSchoolName(schoolMaster.selectById(student.getMasterId()).getAreaName());
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
    public FaceImgInfoVo getFaceImgInfo(String type, Long id) {
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
            return infoVo;
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
            infoVo.setTeacherInfo(teacherInfo);
            return infoVo;
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
                if (null != teacher.getSectionId()) {
                    dto.setSectionId(teacher.getSectionId());
                    dto.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                }
                if (null != teacher.getImgId()) {
                    dto.setImg(sysFileService.getFileById(teacher.getImgId()).getFileUrl());
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
                    return dto;
                }
            } else {
                STDto.TInfo tInfo = new STDto.TInfo();
                SchoolTeacher teacher = teacherMapper.selectById(id);
                if (PublicUtil.isNotEmpty(teacher)) {
                    tInfo.setTId(teacher.getId());
                    tInfo.setTNumber(teacher.getTNumber());
                    if (null != teacher.getSectionId()) {
                        tInfo.setSectionName(sectionMapper.selectById(teacher.getSectionId()).getSectionName());
                    }
                }
                dto.setTInfo(tInfo);
                return dto;
            }
        }
        return null;
    }

    @Override
    public Map getBuildingInfoList(Long masterId, Integer type, Long id) {
        if (null != masterId) {
            if (null == type || 1 == type) {
                List<BuildingNo> list = noMapper.selectList(new QueryWrapper<BuildingNo>().eq("master_id", masterId));
                if (PublicUtil.isNotEmpty(list)) {
                    return list.stream().collect(Collectors.toMap(BuildingNo::getId, BuildingNo::getBuildingNo));
                }
            } else if (2 == type && null != id) {
                List<BuildingLevel> list = levelMapper.selectList(new QueryWrapper<BuildingLevel>().eq("building_no_id", id));
                if (PublicUtil.isNotEmpty(list)) {
                    return list.stream().collect(Collectors.toMap(BuildingLevel::getId, BuildingLevel::getBuildingLevel));
                }
            } else if (3 == type && null != id) {
                List<BuildingRoom> list = roomMapper.selectList(new QueryWrapper<BuildingRoom>().eq("building_level_id", id));
                if (PublicUtil.isNotEmpty(list)) {
                    return list.stream().collect(Collectors.toMap(BuildingRoom::getId, BuildingRoom::getBuildingRoom));
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
    public BuildingInfoListBedVo getStudentsByRoom(Long roomId) {
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
                    }
                    vos.add(bed);
                });
                vo.setBeds(vos);
                return vo;
            }
        }
        return null;
    }
}
