package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.SchoolStudent;
import com.safe.campus.model.dto.SchoolStudentDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.vo.SchoolStudentListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-05
 */
public interface SchoolStudentService extends IService<SchoolStudent> {

    Wrapper saveStudent(SchoolStudentDto dto,LoginAuthDto loginAuthDto);

    Wrapper getStudent(Long id);

    Wrapper editStudent(SchoolStudentDto dto);

    Wrapper deleteStudent(Long id);

    PageWrapper<List<SchoolStudentListVo>> searchStudent(Long masterId, String context, BaseQueryDto baseQueryDto);

    List<SchoolStudent> getAllStudent(Long classInfoId);

    SchoolStudent selectById(Long id);

    List<SchoolStudent> getAllIdsByName(String context);

    Wrapper importSchoolConcentrator(Long masterId,MultipartFile file,  LoginAuthDto loginAuthDto);

    Wrapper importStudentPictureConcentrator(Long masterId,MultipartFile file, LoginAuthDto loginAuthDto);

    PageWrapper<List<SchoolStudentListVo>> listStudent(Integer type,Long masterId, Long classId, BaseQueryDto baseQueryDto);

}
