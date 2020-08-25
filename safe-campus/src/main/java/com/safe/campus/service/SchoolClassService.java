package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    Wrapper saveSchoolClass(SchoolClassDto schoolClassDto);

    Wrapper saveSchoolClassInfo(SchoolClassInfoDto schoolClassInfoDto);

    Wrapper deleteSchoolClass(Integer type, Long id);

    Wrapper operationSchoolClass(Integer state, Long id);

    Wrapper operationSchoolClassInfo(Integer state, Long id);

    Wrapper searchSchoolClass(String name);

    Wrapper nodeTreeSchoolClass();

    Wrapper nodeTreeInfoSchoolClass( Long id);

    List<SchoolClass> getAllClass();

    List<SchoolClassInfo> getAllClassInfo(Long classId);

    Wrapper listClass(Long masterId);

    Wrapper<Map<Long, String>> listTeachers(Integer type);

    Wrapper<SchoolClassEditVo> getInfo(Integer type, Long id);
}
