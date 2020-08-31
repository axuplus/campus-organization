package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.model.domain.SchoolClass;
import com.safe.campus.model.domain.SchoolClassInfo;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.dto.SchoolClassInfoDto;
import com.safe.campus.model.vo.SchoolClassEditVo;
import com.safe.campus.about.utils.wrapper.Wrapper;

import java.util.List;
import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-30
 */
public interface SchoolClassService extends IService<SchoolClass> {

    Wrapper saveSchoolClass(SchoolClassDto schoolClassDto, LoginAuthDto loginAuthDto);

    Wrapper saveSchoolClassInfo(SchoolClassInfoDto schoolClassInfoDto,LoginAuthDto loginAuthDto);

    Wrapper deleteSchoolClass(Integer type, Long id);

    Wrapper searchSchoolClass(Long masterId,String name);

    Wrapper nodeTreeSchoolClass(Long masterId);

    List<SchoolClass> getAllClass(Long masterId);

    List<SchoolClassInfo> getAllClassInfo(Long classId);

    Wrapper listClass(Long masterId, Long id, Integer type, BaseQueryDto baseQueryDto);

    Wrapper<Map<Long, String>> listTeachers(Long masterId);

    Wrapper<SchoolClassEditVo> getInfo(Integer type, Long id);

    Wrapper editClass(SchoolClassDto schoolClassDto, LoginAuthDto loginAuthDto);

    Wrapper editClassInfo(SchoolClassInfoDto schoolClassInfoDto, LoginAuthDto loginAuthDto);

    Wrapper operation(Integer type, Integer state, Long id);
}
