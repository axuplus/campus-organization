package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.model.domain.BuildingNo;
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

    Wrapper saveBuilding(String buildingName);

    Wrapper<BuildingRoomVo> getBuilding(Long id);

    Wrapper<List<BuildingClassVo>> getAllClass();

    Wrapper<List<BuildingClassVo>> getAllClassInfo(Long classId);

    Wrapper<List<BuildingStudentVo>> getAllStudent(Long classInfoId);

    Wrapper editBuilding(Long id, Long studentId);

    Wrapper<List<BuildingTreeVo>> getBuildingTree();

    Wrapper deleteBuilding(Integer type, Long id);

    Wrapper<List<BuildingManagerVo>> managerList(Long id);

    Wrapper<List<BuildingStudentListVo>> searchList(String context);

    Wrapper saveBuildingLevel(Long buildingNoId, Integer level);

    Wrapper saveBuildingRoom(Long buildingLevelId, Integer buildingRoom);

    Wrapper saveBuildingStudent(Long buildingRoomId, Long studentId, Long bedNo);

    Wrapper<List<BuildingStudentListVo>> studentList(Long id);

    Wrapper<Map<Long, String>> getBuildingTeachers();

    Wrapper<BuildingTeacherVo> getBuildingTeacher(Long buildingNoId);

    Wrapper setBuildingTeacher(Long buildingNoId, List<Long> levels, Long teacherId);

    Wrapper deleteBuildingManger(Long id, Long levelId);

    Boolean checkBuildingBedIsFull(String buildingBed,String buildingLevel,String buildingRoom,String buildingNo );

    Wrapper saveBuildingBed(Long buildingRoomId, Integer buildingBed);

    Long getBuildingNoId(String buildingNo);
    Long getBuildingLevelId(Long noId, String buildingLevel);
    Long getBuildingRoomId(Long levelId,String buildingRoom);
    Long getBuildingBedId(Long roomId,String buildingBed);


}
