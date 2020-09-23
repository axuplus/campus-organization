package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SchoolMaster;
import com.safe.campus.model.dto.*;

import java.util.List;

/**
 * <p>
 * 学校总校区表 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-18
 */
public interface SchoolMasterService extends IService<SchoolMaster> {


    Wrapper saveSchool(LoginAuthDto loginAuthDto, SchoolMasterDto schoolMasterDto);

    Wrapper getSchool(LoginAuthDto loginAuthDto, Long id);

    Wrapper editSchool(LoginAuthDto loginAuthDto, SchoolMasterDto schoolMasterDto);

    Wrapper listSchool(LoginAuthDto loginAuthDto);

    Wrapper deleteNode(LoginAuthDto loginAuthDto, Long rootId);

    Wrapper listNode(LoginAuthDto loginAuthDto);

    Wrapper listConf(LoginAuthDto loginAuthDto);

    Wrapper saveConf(LoginAuthDto loginAuthDto, SchoolMaterConfDto schoolMaterConfDto);

    Wrapper listProvince(LoginAuthDto loginAuthDto);

    Wrapper listCity(LoginAuthDto loginAuthDto, Long provinceId);

    Wrapper listArea(LoginAuthDto loginAuthDto, Long cityId);

    Wrapper resetPassword(LoginAuthDto loginAuthDto, String account);

    Wrapper listSchoolConf(LoginAuthDto loginAuthDto, Long masterId);

    Wrapper saveOrEditNode(SaveOrEditNodeDto saveOrEditNodeDto);


    Wrapper saveOrEditIntroduction(SchoolIntroductionDto schoolIntroductionDto);


    Wrapper<SchoolIntroductionDto> getIntroduction(Long masterId);
}
