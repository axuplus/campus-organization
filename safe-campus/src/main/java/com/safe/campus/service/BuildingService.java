package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.BuildingNo;
import com.safe.campus.model.dto.BuildingStudentDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.about.utils.wrapper.Wrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼幢表 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
public interface BuildingService extends IService<BuildingNo> {

    Wrapper saveBuilding(String buildingName, Long masterId, LoginAuthDto loginAuthDto);

    Wrapper<BuildingRoomVo> getBuilding(Long id);

    Wrapper<List<BuildingClassVo>> getAllClass(Long masterId);

    Wrapper<List<BuildingClassVo>> getAllClassInfo(Long classId);

    Wrapper<List<BuildingStudentVo>> getAllStudent(Long classInfoId);

    Wrapper editBuilding(Long id, Long studentId);

    Wrapper<List<BuildingTreeVo>> getBuildingTree(Long masterId);

    Wrapper deleteBuilding(Integer type, Long id);

    PageWrapper<List<BuildingManagerVo>> managerList(Long id, BaseQueryDto baseQueryDto);

    Wrapper searchList(Long masterId,Integer type,String context);

    Wrapper saveBuildingLevel(Long buildingNoId, Integer level,LoginAuthDto loginAuthDto);

    Wrapper saveBuildingRoom(Long buildingLevelId, Integer buildingRoom,LoginAuthDto loginAuthDto);

    Wrapper saveBuildingStudent(BuildingStudentDto buildingStudentDto);

    PageWrapper<List<BuildingStudentListVo>> levelStudentList(Long id, BaseQueryDto baseQueryDto);

    PageWrapper<List<BuildingStudentListVo>> roomStudentList(Long id,BaseQueryDto baseQueryDto);

    Wrapper<Map<Long, String>> getBuildingTeachers(Long masterId);

    Wrapper<BuildingTeacherVo> getBuildingTeacher(Long levelId);

    Wrapper setBuildingTeacher(Long levelId, Long teacherId);

    Wrapper deleteBuildingManger( Long levelId);

    Boolean checkBuildingBedIsFull(String buildingBed,String buildingLevel,String buildingRoom,String buildingNo );

    Wrapper saveBuildingBed(Long buildingRoomId, Integer buildingBed,LoginAuthDto loginAuthDto);
    Long getBuildingNoId(String buildingNo);
    Long getBuildingLevelId(Long noId, String buildingLevel);
    Long getBuildingRoomId(Long levelId,String buildingRoom);

    Long getBuildingBedId(Long roomId,String buildingBed);


}
