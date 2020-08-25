package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    Wrapper saveSchoolSection(SchoolSectionDto schoolSectionDto);

    Wrapper editSchoolSection(SchoolSectionInfoDto schoolSectionInfoDto);

    Wrapper getSchoolSection(Long id);

    Wrapper deleteSchoolSection(Long id);

    Wrapper activeSchoolSection(Long id, Integer type);

    Wrapper listSchoolSection(Long id);

    Wrapper getDetailsSection(Long id);

    Wrapper searchSchoolSection(String name);

    Wrapper<Map<Long, String>> getCharge();

    Wrapper<Map<Long, String>> getSchools();

}
