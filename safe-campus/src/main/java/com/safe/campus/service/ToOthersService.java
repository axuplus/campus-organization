package com.safe.campus.service;

import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.STDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.dto.TeacherByPhoneDto;
import com.safe.campus.model.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ToOthersService {


    Wrapper<Map<Long, String>> getAllMasters();

    Wrapper<Map<Long, String>> getAllClassesByMasterId(Long masterId);

    OthersDto getIdNumberByUserId(String type, Long userId);

    List<SelectStudentListDto> selectStudentList(Map map);

    String getTeacherRoles(Long teacherId);

    OthersStudentVo getStudentByIdNumber(String idNumber);

    Map getStudentTeacherByIdNumber(String idNumber);

    Wrapper<FaceImgInfoVo> getFaceImgInfo(String type, Long id);

    OthersTeacherVo getTeacherForMiniApp(String idNumber);

    TeacherByPhoneDto getTeacherByPhone(String teacherName, String phone);

    STDto getStudentAndTeacherInfo(Integer type, Long id);

    Wrapper<Map> getBuildingInfoList( Long masterId,Integer type, Long id);

    PageWrapper<List<BuildingInfoListRoomVo>> getBuildingInfoListByIds(Long masterId, Integer type, Long id, BaseQueryDto baseQueryDto);

    Wrapper<BuildingInfoListBedVo> getStudentsByRoom(Long roomId);

    List<Object> getStudentsOrTeachersByType(Long masterId, Integer type);

    List<ListStudentByTeacherVo> getStudentByTeacherId(Long teacherId);

    StudentCountVo getStudentCountByTeacherPhone(String tName, Long phteacherIdone);

    PageWrapper<List<AllStudentsVo>> getAllStudents(BaseQueryDto baseQueryDto);

    PageWrapper<List<AllTeachersVo>> getAllTeachers(BaseQueryDto baseQueryDto);

    Wrapper<List<BuildingClassVo>> getAllBuildings(Long schoolId);

    Wrapper<WhiteListVo> getPersonsByType(Long schoolId,Integer type);

    Wrapper<List<BuildingNoWithLevelVo>> getBuildingNoAndLevels(String schoolId);

    Map getStudentTeacherByStudentId(Long studentId);

    Wrapper getStudentInfoById(String studentId);

    PageWrapper<Object> getPersonsForDoc(Long schoolId, Integer type,String context,BaseQueryDto baseQueryDto, LoginAuthDto loginAuthDto);

    Wrapper getStudentsByTeacherId(Long teacherId);
}
