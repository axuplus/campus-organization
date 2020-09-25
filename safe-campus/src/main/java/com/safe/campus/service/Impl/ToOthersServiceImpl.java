package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.vo.FaceImgInfoVo;
import com.safe.campus.model.vo.OthersStudentVo;
import com.safe.campus.service.SysFileService;
import com.safe.campus.service.ToOthersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<String> getTeacherRoles(Long teacherId) {
        List<String> list = new ArrayList<>();
        SysAdmin admin = adminUserMapper.selectOne(new QueryWrapper<SysAdmin>().eq("t_id", teacherId));
        if (PublicUtil.isEmpty(admin)) {
            return list;
        }
        List<String> roleNames = userRoleMapper.getRoleNamesByUserId(admin.getId());
        if (PublicUtil.isEmpty(roleNames)) {
            return list;
        }
        roleNames.forEach(n -> {
            // "G:访客,T:老师,P:家长,H:班主任,S:保安,D:宿管"
            if ("班主任".equals(n)) {
                list.add("H");
            } else if ("宿管".equals(n)) {
                list.add("D");
            } else if ("保安".equals(n)) {
                list.add("S");
            }
        });
        return list;
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
            studentVo.setSIdNumber(student.getIdNumber());
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
    public Map getTeacherForMiniApp(String idNumber) {
        SchoolTeacher teacher = teacherMapper.selectOne(new QueryWrapper<SchoolTeacher>().eq("id_number", idNumber));
        if (PublicUtil.isNotEmpty(teacher)) {
            Map<Long, Long> map = new HashMap<>();
            map.put(teacher.getId(), teacher.getMasterId());
            return map;
        }
        return null;
    }
}
