package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.SchoolClass;
import com.safe.campus.model.domain.SchoolMaster;
import com.safe.campus.model.domain.SchoolStudent;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.service.ToOthersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private SchoolClassInfoMapper infoMapper;

    @Autowired
    private SchoolStudentMapper studentMapper;

    @Autowired
    private SchoolTeacherMapper teacherMapper;

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
                    list.add(dto);
                });
                return list;
            }
        }
        return null;
    }
}
