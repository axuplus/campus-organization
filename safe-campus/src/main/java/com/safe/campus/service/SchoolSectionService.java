package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.SchoolSection;
import com.safe.campus.model.dto.SchoolSectionDto;
import com.safe.campus.model.dto.SchoolSectionInfoDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.vo.SchoolSectionVo;
import com.safe.campus.model.vo.SectionTeachersVo;
import com.safe.campus.model.vo.SectionVo;

import java.util.List;
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

    PageWrapper<List<SchoolSectionVo>> listSchoolSection(Long masterId,Integer type, Long id, BaseQueryDto baseQueryDto);

    PageWrapper<List<SchoolSectionVo>> searchSchoolSection(Long masterId,String name,BaseQueryDto baseQueryDto);

    Wrapper<List<SectionTeachersVo>> getCharge(Long masterId);

    Wrapper<Map<Long, String>> getSchools(Long masterId, LoginAuthDto loginAuthDto);

    Wrapper<List<SectionVo>> getSuperior( Long masterId);

}
