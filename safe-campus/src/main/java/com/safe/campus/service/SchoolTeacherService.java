package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.dto.SetRoleDto;
import com.safe.campus.model.dto.TeacherInfoDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
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

    Wrapper listTeacherInfo(Long id);

    Wrapper deleteTeacherInfo(Long id);

    Wrapper getTeacherInfo(Long id);

    Wrapper saveTeacherInfo(TeacherInfoDto teacherInfoDto, LoginAuthDto loginAuthDto);

    Wrapper editTeacherInfo(TeacherInfoDto teacherInfoDto);

    Wrapper importTeacherConcentrator(MultipartFile file,LoginAuthDto loginAuthDto);

    Wrapper searchTeacherInfo(String context);

    List<SchoolTeacher> getCharge(Long masterId);

    SchoolTeacher getTeacherBySection(Long sectionId);

    void updateSectionTeacher(Long sectionId,Long tId);

    void addSectionId(Long sectionId,Long tId);

    SchoolTeacher getTeacher(Long tId);

    List<SchoolTeacher> getTeachersToClass(Long masterId);

    List<SchoolTeacher> getBuildingTeachers();

    Wrapper listRoles(LoginAuthDto loginAuthDto);

    Wrapper setRole(LoginAuthDto loginAuthDto, SetRoleDto setRoleDto);

    Wrapper importTeacherPictureConcentrator(MultipartFile file, LoginAuthDto loginAuthDto);

    Wrapper active(LoginAuthDto loginAuthDto, Long id, Long masterId,Integer state);
}
