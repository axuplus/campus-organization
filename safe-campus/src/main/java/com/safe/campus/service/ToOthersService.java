package com.safe.campus.service;

import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;
import com.safe.campus.model.vo.FaceImgInfoVo;
import com.safe.campus.model.vo.OthersStudentVo;
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

    Map getTeacherForMiniApp(String idNumber);

}
