package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.vo.SchoolTeacherSectionVo;
import com.safe.campus.model.vo.SchoolTeacherVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 教职工信息 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-03
 */
public interface SchoolTeacherService extends IService<SchoolTeacher> {

    Wrapper saveTeacherInfo(TeacherInfoDto teacherInfoDto, LoginAuthDto loginAuthDto);

    PageWrapper<List<SchoolTeacherVo>> listTeacherInfo(Integer type,Long masterId, Long id, BaseQueryDto baseQueryDto);

    Wrapper deleteTeacherInfo(Long id);

    Wrapper<SchoolTeacherVo> getTeacherInfo(Long id);

    Wrapper editTeacherInfo(TeacherInfoDto teacherInfoDto);

    Wrapper importTeacherConcentrator(MultipartFile file,LoginAuthDto loginAuthDto);

    PageWrapper<List<SchoolTeacherVo>> searchTeacherInfo(Long masterId,String context,BaseQueryDto baseQueryDto);

    List<SchoolTeacher> getCharge(Long masterId);

    SchoolTeacher getTeacherBySection(Long id);

    SchoolTeacher getTeacher(Long tId);

    List<SchoolTeacher> getTeachersToClass(Long masterId);

    Wrapper listRoles(Long masterId,LoginAuthDto loginAuthDto);

    Wrapper setRole(LoginAuthDto loginAuthDto, SetRoleDto setRoleDto);

    Wrapper importTeacherPictureConcentrator(MultipartFile file, LoginAuthDto loginAuthDto);

    Wrapper active(LoginAuthDto loginAuthDto, Long id, Long masterId,Integer state);

    List<SchoolTeacher> searchTeachersByName(String context,Long masterId);

    Wrapper<List<SchoolTeacherSectionVo>> getSection(Long masterId, Long sectionId);

}
