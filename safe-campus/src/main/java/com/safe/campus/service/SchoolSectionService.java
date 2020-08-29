package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.model.domain.SchoolSection;
import com.safe.campus.model.dto.SchoolSectionDto;
import com.safe.campus.model.dto.SchoolSectionInfoDto;
import com.safe.campus.about.utils.wrapper.Wrapper;

import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-28
 */
public interface SchoolSectionService extends IService<SchoolSection> {

    Wrapper getDetailsSection(Long id);

    Wrapper saveSchoolSection(SchoolSectionDto schoolSectionDto);

    Wrapper editSchoolSection(SchoolSectionInfoDto schoolSectionInfoDto);

    Wrapper getSchoolSection(Long id);

    Wrapper deleteSchoolSection(Long id);

    Wrapper activeSchoolSection(Long id, Integer type);

    Wrapper listSchoolSection(Long masterId, Long id, BaseQueryDto baseQueryDto);

    Wrapper searchSchoolSection(Long masterId,String name);

    Wrapper<Map<Long, String>> getCharge(Long masterId);

    Wrapper<Map<Long, String>> getSchools(Long masterId, LoginAuthDto loginAuthDto);

}
