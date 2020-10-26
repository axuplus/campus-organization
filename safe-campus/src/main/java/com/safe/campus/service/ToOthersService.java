package com.safe.campus.service;

import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.STDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.dto.TeacherByPhoneDto;
import com.safe.campus.model.vo.*;

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

    FaceImgInfoVo getFaceImgInfo(String type, Long id);

    OthersTeacherVo getTeacherForMiniApp(String idNumber);

    TeacherByPhoneDto getTeacherByPhone(String teacherName, String phone);

    STDto getStudentAndTeacherInfo(Integer type, Long id);

    Map getBuildingInfoList( Long masterId,Integer type, Long id);

    PageWrapper<List<BuildingInfoListRoomVo>> getBuildingInfoListByIds(Long masterId, Integer type, Long id, BaseQueryDto baseQueryDto);

    BuildingInfoListBedVo getStudentsByRoom(Long roomId);

    List<Object> getStudentsOrTeachersByType(Long masterId, Integer type);

    List<ListStudentByTeacherVo> getStudentByTeacherId(Long teacherId);

    StudentCountVo getStudentCountByTeacherPhone(String tName, String phone);

    PageWrapper<List<AllStudentsVo>> getAllStudents(Long schoolId, BaseQueryDto baseQueryDto);

    PageWrapper<List<AllTeachersVo>> getAllTeachers(Long schoolId, BaseQueryDto baseQueryDto);

    Wrapper<List<BuildingClassVo>> getAllBuildings(Long schoolId);

    Wrapper<WhiteListVo> getPersonsByType(Long schoolId,Integer type);

}
