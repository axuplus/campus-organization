package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.model.domain.BuildingNo;
import com.safe.campus.model.dto.BuildingBedDto;
import com.safe.campus.model.dto.BuildingNoMapperDto;
import com.safe.campus.model.dto.BuildingStudentDto;
import com.safe.campus.model.dto.SaveBuildingInfoDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.about.utils.wrapper.Wrapper;
import org.springframework.web.multipart.MultipartFile;

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

    Wrapper saveBuilding(SaveBuildingInfoDto saveBuildingInfoDto, LoginAuthDto loginAuthDto);

    Wrapper<List<BuildingClassVo>> getAllClass(Long masterId);

    Wrapper<List<BuildingClassVo>> getAllClassInfo(Long classId);

    Wrapper<List<BuildingStudentVo>> getAllStudent(Long classInfoId);

    Wrapper<List<BuildingTreeVo>> getBuildingTree(Long masterId);

    Wrapper deleteBuilding(Integer type, Long id);

    PageWrapper<List<BuildingManagerVo>> managerList(Long id, Long masterId,BaseQueryDto baseQueryDto);

    Wrapper searchList(Long masterId,Integer type,String context);

    Wrapper saveBuildingStudent(BuildingStudentDto buildingStudentDto);

    Wrapper <List<SchoolClassTeachersVo>> getBuildingTeachers(Long masterId);

    Wrapper<BuildingTeacherVo> getBuildingTeacher(Long levelId);

    Wrapper setBuildingTeacher(Long levelId, Long teacherId);

    Wrapper deleteBuildingManger( Long levelId);

    BuildingBedDto getLivingInfoByStudentId(Long id);

    BuildingNoMapperDto checkBuildingInfo(Long masterId, String buildingNo, String buildingLevel, String buildingRoom, String buildingBed);

    PageWrapper<List<BuildingStudentListVo>> studentList(Integer type, Long id,Long masterId, BaseQueryDto baseQueryDto);

    Wrapper editBuildingTree(Integer type, Long id, String name);

    Wrapper deleteBuildingStudent(Long sId);

    List<SchoolStudentBuildingVo> getStudentBuildingInfo(Long masterId,Integer type, Long id);

    Wrapper importBuildingInfo(MultipartFile file,LoginAuthDto loginAuthDto);

}
