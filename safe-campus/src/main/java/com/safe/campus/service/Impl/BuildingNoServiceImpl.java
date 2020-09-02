package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.BuildingBedDto;
import com.safe.campus.model.dto.BuildingNoMapperDto;
import com.safe.campus.model.dto.BuildingStudentDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.BuildingService;
import com.safe.campus.service.SchoolClassService;
import com.safe.campus.service.SchoolStudentService;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.service.GobalInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 楼幢表 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@Service
public class BuildingNoServiceImpl extends ServiceImpl<BuildingNoMapper, BuildingNo> implements BuildingService {


    @Autowired
    private BuildingNoMapper noMapper;

    @Autowired
    private BuildingRoomMapper roomMapper;

    @Autowired
    private BuildingLevelMapper levelMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SchoolClassService classService;

    @Autowired
    private SchoolStudentService studentService;

    @Autowired
    private SchoolTeacherService teacherService;

    @Autowired
    private BuildingStudentMapper buildingStudentMapper;

    @Autowired
    private BuildingBedMapper bedMapper;


    @Override
    public Wrapper saveBuilding(String buildingName, Long masterId, LoginAuthDto loginAuthDto) {
        if (null != buildingName) {
            BuildingNo buildingNo = new BuildingNo();
            buildingNo.setId(gobalInterface.generateId());
            buildingNo.setMasterId(masterId);
            buildingNo.setBuildingNo(buildingName);
            buildingNo.setCreateTime(new Date());
            buildingNo.setIsDelete(0);
            buildingNo.setState(0);
            buildingNo.setCreateUser(loginAuthDto.getUserId());
            noMapper.insert(buildingNo);
            return WrapMapper.ok("保存成功");
        }
        return null;
    }

    @Override
    public Wrapper saveBuildingLevel(Long buildingNoId, Integer level, LoginAuthDto loginAuthDto) {
        if (null == buildingNoId || null == level) {
            return WrapMapper.error("参数不能为空");
        }
        BuildingLevel buildingLevel = new BuildingLevel();
        buildingLevel.setId(gobalInterface.generateId());
        buildingLevel.setBuildingLevel(level);
        buildingLevel.setBuildingNoId(buildingNoId);
        buildingLevel.setIsDelete(0);
        buildingLevel.setState(0);
        buildingLevel.setCreateUser(loginAuthDto.getUserId());
        buildingLevel.setCreateTime(new Date());
        levelMapper.insert(buildingLevel);
        return WrapMapper.ok("添加成功");
    }

    @Override
    public Wrapper saveBuildingRoom(Long buildingLevelId, Integer buildingRoom, LoginAuthDto loginAuthDto) {
        if (null == buildingLevelId || null == buildingRoom) {
            return WrapMapper.error("参数有误");
        }
        BuildingRoom room = new BuildingRoom();
        room.setId(gobalInterface.generateId());
        room.setBuildingLevelId(buildingLevelId);
        room.setBuildingRoom(buildingRoom);
        room.setIsDelete(0);
        room.setState(0);
        room.setCreateUser(loginAuthDto.getUserId());
        room.setCreateTime(new Date());
        roomMapper.insert(room);
        return WrapMapper.ok("保存成功");
    }

    @Override
    public Wrapper saveBuildingBed(Long buildingRoomId, Integer buildingBed, LoginAuthDto loginAuthDto) {
        if (null != buildingRoomId && null != buildingBed) {
            BuildingBed bed = new BuildingBed();
            bed.setId(gobalInterface.generateId());
            bed.setBedName(buildingBed);
            bed.setRoomId(buildingRoomId);
            bedMapper.insert(bed);
            return WrapMapper.ok();
        }
        return null;
    }

    @Override
    public Wrapper saveBuildingStudent(BuildingStudentDto buildingStudentDto) {
        if (PublicUtil.isNotEmpty(buildingStudentDto)) {
            return WrapMapper.error("参数不能为空");
        }
        BuildingStudent student = new BuildingStudent();
        student.setId(gobalInterface.generateId());
        student.setStudentId(buildingStudentDto.getStudentId());
        student.setNoId(buildingStudentDto.getBuildingNoId());
        student.setLevelId(buildingStudentDto.getBuildingLevelId());
        student.setRoomId(buildingStudentDto.getBuildingRoomId());
        student.setBedId(buildingStudentDto.getBedId());
        student.setIsDelete(0);
        student.setCreateTime(new Date());
        buildingStudentMapper.insert(student);
        return WrapMapper.ok("保存成功");
    }

    @Override
    public PageWrapper<List<BuildingStudentListVo>> levelStudentList(Long id, BaseQueryDto baseQueryDto) {
        QueryWrapper<BuildingStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level_id", id);
        Page page = PageHelper.startPage(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
        List<BuildingStudent> buildingStudents = buildingStudentMapper.selectList(queryWrapper);
        Long total = page.getTotal();
        if (PublicUtil.isNotEmpty(buildingStudents)) {
            List<BuildingStudentListVo> list = new ArrayList<>();
            buildingStudents.forEach(s -> {
                BuildingStudentListVo listVo = new BuildingStudentListVo();
                listVo.setId(s.getId());
                listVo.setBedNo(bedMapper.selectById(s.getBedId()).getBedName());
                BuildingRoom room = roomMapper.selectById(s.getRoomId());
                listVo.setBuildingRoom(room.getBuildingRoom());
                BuildingLevel level = levelMapper.selectById(room.getBuildingLevelId());
                listVo.setBuildingLevel(level.getBuildingLevel());
                listVo.setBuildingNo(noMapper.selectById(level.getBuildingNoId()).getBuildingNo());
                SchoolStudent student = studentService.selectById(s.getStudentId());
                if (PublicUtil.isNotEmpty(student)) {
                    listVo.setState(1);
                    listVo.setSName(student.getSName());
                    listVo.setSNumber(student.getSNumber());
                    SchoolStudent selectById = studentService.selectById(s.getStudentId());
                    listVo.setClassInfo(selectById.getClassName() + selectById.getClassInfoName());
                } else {
                    listVo.setState(0);
                }
                list.add(listVo);
            });
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
        }
        return null;
    }


    @Override
    public Wrapper<BuildingRoomVo> getBuilding(Long id) {
        if (null != id) {
            BuildingStudent student = buildingStudentMapper.selectById(id);
            if (PublicUtil.isNotEmpty(student)) {
                BuildingRoomVo vo = new BuildingRoomVo();
                vo.setId(student.getId());
                vo.setBuildingNo(noMapper.selectById(student.getNoId()).getBuildingNo());
                vo.setBuildingRoom(roomMapper.selectById(student.getRoomId()).getBuildingRoom());
                vo.setBedNo(bedMapper.selectById(student.getBedId()).getBedName());
                vo.setBuildingLevel(levelMapper.selectById(student.getLevelId()).getBuildingLevel());
                vo.setStudentId(student.getStudentId());
                SchoolStudent schoolStudent = studentService.selectById(student.getStudentId());
                if (PublicUtil.isNotEmpty(schoolStudent)) {
                    vo.setSNumber(schoolStudent.getSNumber());
                    vo.setSName(schoolStudent.getSName());
                    if (1 == schoolStudent.getSex()) {
                        vo.setSex("男");
                    } else {
                        vo.setSex("女");
                    }
                }
                BuildingLevel buildingInfo = levelMapper.selectById(roomMapper.selectById(student.getRoomId()));
                if (null != buildingInfo) {
                    BuildingNo buildingNo = noMapper.selectById(buildingInfo.getBuildingNoId());
                    vo.setBuildingLevel(buildingInfo.getBuildingLevel());
                    if (null != buildingNo) {
                        vo.setBuildingNo(buildingNo.getBuildingNo());
                    }
                }
                return WrapMapper.ok(vo);
            }
        }
        return null;
    }

    @Override
    public Wrapper<List<BuildingClassVo>> getAllClass(Long masterId) {
        List<SchoolClass> allClass = classService.getAllClass(masterId);
        if (PublicUtil.isNotEmpty(allClass)) {
            List<BuildingClassVo> list = new ArrayList<>();
            allClass.forEach(all -> {
                list.add(new ModelMapper().map(all, BuildingClassVo.class));
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无年级数据");
    }

    @Override
    public Wrapper<List<BuildingClassVo>> getAllClassInfo(Long classId) {
        List<SchoolClassInfo> allClassInfo = classService.getAllClassInfo(classId);
        if (PublicUtil.isNotEmpty(allClassInfo)) {
            List<BuildingClassVo> list = new ArrayList<>();
            allClassInfo.forEach(all -> {
                list.add(new ModelMapper().map(all, BuildingClassVo.class));
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无班级数据");
    }

    @Override
    public Wrapper<List<BuildingStudentVo>> getAllStudent(Long classInfoId) {
        List<SchoolStudent> allStudent = studentService.getAllStudent(classInfoId);
        if (PublicUtil.isNotEmpty(allStudent)) {
            List<BuildingStudentVo> list = new ArrayList<>();
            allStudent.forEach(s -> {
                BuildingStudentVo map = new ModelMapper().map(s, BuildingStudentVo.class);
                if (1 == s.getSex()) {
                    map.setSex("男");
                } else {
                    map.setSex("女");
                }
                list.add(map);
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无学生数据");
    }

    @Override
    public Wrapper editBuilding(Long id, Long studentId) {
        if (null != studentId) {
            QueryWrapper<BuildingStudent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            BuildingStudent buildingStudent = buildingStudentMapper.selectById(queryWrapper);
            if (PublicUtil.isNotEmpty(buildingStudent)) {
                buildingStudent.setStudentId(studentId);
                buildingStudentMapper.updateById(buildingStudent);
                return WrapMapper.ok("修改成功");
            }
            return WrapMapper.error("修改失败");
        }
        return null;
    }


    @Override
    public Wrapper deleteBuilding(Integer type, Long id) {
        if (null != type && null != id) {
            if (1 == type) {
                QueryWrapper<BuildingLevel> levelQueryWrapper = new QueryWrapper<>();
                levelQueryWrapper.eq("building_no_id", id);
                List<BuildingLevel> levels = levelMapper.selectList(levelQueryWrapper);
                if (PublicUtil.isEmpty(levels)) {
                    noMapper.deleteById(id);
                    return WrapMapper.ok("楼幢删除成功");
                } else {
                    return WrapMapper.error("删除失败,楼幢下面还有其他楼层信息");
                }
            } else if (2 == type) {
                QueryWrapper<BuildingRoom> roomQueryWrapper = new QueryWrapper<>();
                roomQueryWrapper.eq("building_level_id", id);
                List<BuildingRoom> rooms = roomMapper.selectList(roomQueryWrapper);
                if (PublicUtil.isEmpty(rooms)) {
                    levelMapper.deleteById(id);
                    return WrapMapper.ok("删除成功");
                } else {
                    return WrapMapper.error("不可删除,楼层下面还有其他宿舍信息");
                }
            } else if (3 == type) {
                roomMapper.deleteById(id);
                return WrapMapper.ok("删除成功");
            } else if (4 == type) {
                buildingStudentMapper.deleteById(id);
                return WrapMapper.ok("删除成功");
            }
        }
        return WrapMapper.error("参数有误");
    }

    @Override
    public PageWrapper<List<BuildingManagerVo>> managerList(Long id, BaseQueryDto baseQueryDto) {
        if (null != id) {
            List<BuildingManagerVo> list = new ArrayList<>();
            BuildingNo buildingNo = noMapper.selectById(id);
            QueryWrapper<BuildingLevel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("building_no_id", id);
            Page page = PageHelper.startPage(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
            List<BuildingLevel> levels = levelMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(levels)) {
                levels.forEach(level -> {
                    BuildingManagerVo buildingManagerVo = new BuildingManagerVo();
                    buildingManagerVo.setBuildingLevelId(level.getId());
                    buildingManagerVo.setBuildingNo(buildingNo.getBuildingNo());
                    buildingManagerVo.setBuildingLevel(level.getBuildingLevel());
                    SchoolTeacher teacher = teacherService.getTeacher(level.getTId());
                    if (PublicUtil.isNotEmpty(teacher)) {
                        buildingManagerVo.setManagerId(teacher.getId());
                        buildingManagerVo.setManagerName(teacher.getTName());
                        buildingManagerVo.setManagerNo(teacher.getTNumber());
                        buildingManagerVo.setManagerPhone(teacher.getPhone());
                    }
                    list.add(buildingManagerVo);
                });
            }
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
        }
        return null;
    }


    @Override
    public Wrapper searchList(Long masterId, Integer type, String context) {
        if (null == context) {
            return WrapMapper.error("请输入搜索条件");
        }
        if (2 == type) {
            List<BuildingStudentListVo> list = new ArrayList<>();
            List<SchoolStudent> allIdsByName = studentService.getAllIdsByName(context);
            if (PublicUtil.isNotEmpty(allIdsByName)) {
                allIdsByName.forEach(student -> {
                    BuildingStudentListVo vo = new BuildingStudentListVo();
                    vo.setSName(student.getSName());
                    vo.setSNumber(student.getSNumber());
                    vo.setClassInfo(student.getClassName() + student.getClassInfoName());
                    QueryWrapper<BuildingStudent> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("student_id", student.getId());
                    BuildingStudent selectOne = buildingStudentMapper.selectOne(queryWrapper);
                    vo.setId(selectOne.getId());
                    vo.setBedNo(bedMapper.selectById(selectOne.getBedId()).getBedName());
                    BuildingRoom room = roomMapper.selectById(selectOne.getRoomId());
                    vo.setBuildingRoom(room.getBuildingRoom());
                    BuildingLevel level = levelMapper.selectById(room.getBuildingLevelId());
                    vo.setBuildingLevel(level.getBuildingLevel());
                    BuildingNo buildingNo = noMapper.selectById(level.getBuildingNoId());
                    vo.setBuildingNo(buildingNo.getBuildingNo());
                    list.add(vo);
                });
                return WrapMapper.ok(list);
            }
        } else {
            List<SchoolTeacher> teachers = teacherService.searchTeachersByName(context, masterId);
            System.out.println("teachers = " + teachers);
            if (PublicUtil.isNotEmpty(teachers)) {
                List<BuildingManagerVo> list = new ArrayList<>();
                teachers.forEach(t -> {
                    QueryWrapper<BuildingLevel> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("t_id", t.getId());
                    BuildingLevel level = levelMapper.selectOne(queryWrapper);
                    System.out.println("level = " + level);
                    if (PublicUtil.isNotEmpty(level)) {
                        BuildingManagerVo vo = new BuildingManagerVo();
                        vo.setManagerId(t.getId());
                        vo.setManagerPhone(t.getPhone());
                        vo.setManagerNo(t.getTNumber());
                        vo.setManagerName(t.getTName());
                        vo.setBuildingLevel(level.getBuildingLevel());
                        vo.setBuildingLevelId(level.getId());
                        vo.setBuildingNo(noMapper.selectById(level.getBuildingNoId()).getBuildingNo());
                        list.add(vo);
                    }
                });
                return WrapMapper.ok(list);
            }
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public PageWrapper<List<BuildingStudentListVo>> roomStudentList(Long id, BaseQueryDto baseQueryDto) {
        if (null != id) {
            QueryWrapper<BuildingStudent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", id);
            Page page = PageHelper.startPage(baseQueryDto.getPageNum(), baseQueryDto.getPageSize());
            List<BuildingStudent> buildingStudents = buildingStudentMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(buildingStudents)) {
                List<BuildingStudentListVo> list = new ArrayList<>();
                buildingStudents.forEach(s -> {
                    BuildingStudentListVo listVo = new BuildingStudentListVo();
                    listVo.setId(s.getId());
                    listVo.setBedNo(bedMapper.selectById(s.getBedId()).getBedName());
                    BuildingRoom room = roomMapper.selectById(s.getRoomId());
                    listVo.setBuildingRoom(room.getBuildingRoom());
                    BuildingLevel level = levelMapper.selectById(room.getBuildingLevelId());
                    listVo.setBuildingLevel(level.getBuildingLevel());
                    listVo.setBuildingNo(noMapper.selectById(level.getBuildingNoId()).getBuildingNo());
                    SchoolStudent student = studentService.selectById(s.getStudentId());
                    if (PublicUtil.isNotEmpty(student)) {
                        listVo.setState(1);
                        listVo.setSName(student.getSName());
                        listVo.setSNumber(student.getSNumber());
                        SchoolStudent selectById = studentService.selectById(s.getStudentId());
                        listVo.setClassInfo(selectById.getClassName() + selectById.getClassInfoName());
                    } else {
                        listVo.setState(0);
                    }
                    list.add(listVo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPageNum(), baseQueryDto.getPageSize()));
            }
        }
        return null;
    }

    @Override
    public Wrapper<Map<Long, String>> getBuildingTeachers(Long masterId) {
        List<SchoolTeacher> teachers = teacherService.getBuildingTeachers(masterId);
        if (PublicUtil.isNotEmpty(teachers)) {
            return WrapMapper.ok(teachers.stream().collect(Collectors.toMap(SchoolTeacher::getId, SchoolTeacher::getTName)));
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<BuildingTeacherVo> getBuildingTeacher(Long levelId) {
        if (null != levelId) {
            BuildingLevel level = levelMapper.selectById(levelId);
            BuildingTeacherVo vo = new BuildingTeacherVo();
            vo.setLevelId(levelId);
            vo.setLevel(level.getBuildingLevel());
            BuildingNo no = noMapper.selectById(level.getBuildingNoId());
            vo.setBuildingNo(no.getBuildingNo());
            vo.setBuildingId(no.getId());
            SchoolTeacher teacher = teacherService.getTeacher(level.getTId());
            if (PublicUtil.isNotEmpty(teacher)) {
                vo.setTName(teacher.getTName());
            }
            return WrapMapper.ok(vo);
        }
        return null;
    }

    @Override
    public Wrapper setBuildingTeacher(Long levelId, Long teacherId) {
        BuildingLevel level = levelMapper.selectById(levelId);
        level.setTId(teacherId);
        levelMapper.updateById(level);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper deleteBuildingManger(Long levelId) {
        BuildingLevel level = levelMapper.selectById(levelId);
        level.setTId(0L);
        levelMapper.updateById(level);
        return WrapMapper.ok("删除成功");
    }

    @Override
    public BuildingBedDto getLivingInfoByStudentId(Long id) {
        BuildingStudent buildingStudent = buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("student_id", id));
        return bedMapper.getLivingInfo(buildingStudent.getBedId());

    }

    @Override
    public BuildingNoMapperDto checkBuildingInfo(Long masterId, String buildingNo, String buildingLevel, String buildingRoom, String buildingBed) {
      return  noMapper.checkBuildingInfo(masterId,buildingNo,buildingLevel,buildingRoom,buildingBed);
    }

    @Override
    public Wrapper<List<BuildingTreeVo>> getBuildingTree(Long masterId) {
        List<BuildingTreeVo> list = new ArrayList<>();
        // 1：第一层
        QueryWrapper<BuildingNo> noQueryWrapper = new QueryWrapper<>();
        noQueryWrapper.eq("is_delete", 0).eq("master_id", masterId);
        List<BuildingNo> nos = noMapper.selectList(noQueryWrapper);
        if (PublicUtil.isNotEmpty(nos)) {
            nos.forEach(no -> {
                BuildingTreeVo buildingNo = new BuildingTreeVo();
                buildingNo.setBuildingNoId(no.getId());
                buildingNo.setBuildingNoName(no.getBuildingNo());
                buildingNo.setBuildingLevel(getLevels(no.getId()));
                list.add(buildingNo);
            });
            return WrapMapper.ok(list);
        }
        return null;
    }


    // 2：第二层
    private List<BuildingTreeVo.BuildingLevel> getLevels(Long id) {
        QueryWrapper<BuildingLevel> levelQueryWrapper = new QueryWrapper<>();
        levelQueryWrapper.eq("building_no_id", id);
        List<BuildingLevel> levels = levelMapper.selectList(levelQueryWrapper);
        if (PublicUtil.isNotEmpty(levels)) {
            List<BuildingTreeVo.BuildingLevel> list = new ArrayList<>();
            levels.forEach(level -> {
                BuildingTreeVo.BuildingLevel buildingLevel = new BuildingTreeVo.BuildingLevel();
                buildingLevel.setBuildingLevelId(level.getId());
                buildingLevel.setBuildingLevelName(level.getBuildingLevel());
                buildingLevel.setBuildingRoom(getRooms(level.getId()));
                list.add(buildingLevel);
            });
            return list;
        }
        return null;
    }

    // 3：第三层
    private List<BuildingTreeVo.BuildingLevel.BuildingRoom> getRooms(Long id) {
        QueryWrapper<BuildingRoom> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("building_level_id", id);
        List<BuildingRoom> rooms = roomMapper.selectList(roomQueryWrapper);
        if (PublicUtil.isNotEmpty(rooms)) {
            List<BuildingTreeVo.BuildingLevel.BuildingRoom> list = new ArrayList<>();
            rooms.forEach(room -> {
                BuildingTreeVo.BuildingLevel.BuildingRoom buildingRoom = new BuildingTreeVo.BuildingLevel.BuildingRoom();
                buildingRoom.setBuildingRoomId(room.getId());
                buildingRoom.setBuildingRoomName(room.getBuildingRoom());
                list.add(buildingRoom);
            });
            return list;
        }
        return null;
    }
}
