package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.SchoolClass;
import com.safe.campus.model.domain.SchoolClassInfo;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.vo.NodeTreeVo;
import com.safe.campus.model.vo.SchoolClassEditVo;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.vo.SchoolClassSearchVo;
import com.safe.campus.model.vo.SchoolClassTeachersVo;

import java.util.List;
import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-30
 */
public interface SchoolClassService extends IService<SchoolClass> {

    Wrapper saveSchoolClass(SchoolClassDto schoolClassDto, LoginAuthDto loginAuthDto);

    Wrapper deleteSchoolClass(Integer type, Long id);

    Wrapper <List<SchoolClassSearchVo>> searchSchoolClass(Long masterId,String name);

    Wrapper <List<NodeTreeVo>> nodeTreeSchoolClass(Long masterId);

    List<SchoolClass> getAllClass(Long masterId);

    List<SchoolClassInfo> getAllClassInfo(Long classId);

    Wrapper<List<SchoolClassSearchVo>> listClass(Long masterId, Long id, Integer type);

    Wrapper <List<SchoolClassTeachersVo>> listTeachers(Long masterId);

    Wrapper<SchoolClassEditVo> getInfo(Integer type, Long id);

    Wrapper editClass(Long id,Long tId,String name,Integer type, LoginAuthDto loginAuthDto);

    Wrapper operation(Integer type, Integer state, Long id);

    String getClassAndInfo(Long classId, Long classInfoId);
}
