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
import com.safe.campus.model.dto.*;
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

    @Autowired
    private SchoolSectionMapper sectionMapper;


    @Override
    public Wrapper saveBuilding(SaveBuildingInfoDto saveBuildingInfoDto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isNotEmpty(saveBuildingInfoDto)) {
            Long id = null;
            if (1 == saveBuildingInfoDto.getType()) {
                BuildingNo buildingNo = new BuildingNo();
                buildingNo.setId(gobalInterface.generateId());
                buildingNo.setMasterId(saveBuildingInfoDto.getMasterId());
                buildingNo.setBuildingNo(saveBuildingInfoDto.getName());
                buildingNo.setCreateTime(new Date());
                buildingNo.setIsDelete(0);
                buildingNo.setState(0);
                buildingNo.setCreateUser(loginAuthDto.getUserId());
                noMapper.insert(buildingNo);
                id = buildingNo.getId();
            } else if (2 == saveBuildingInfoDto.getType()) {
                BuildingLevel buildingLevel = new BuildingLevel();
                buildingLevel.setId(gobalInterface.generateId());
                buildingLevel.setBuildingLevel(saveBuildingInfoDto.getName());
                buildingLevel.setBuildingNoId(saveBuildingInfoDto.getId());
                buildingLevel.setIsDelete(0);
                buildingLevel.setState(0);
                buildingLevel.setCreateUser(loginAuthDto.getUserId());
                buildingLevel.setCreateTime(new Date());
                levelMapper.insert(buildingLevel);
                id = buildingLevel.getId();
            } else if (3 == saveBuildingInfoDto.getType()) {
                BuildingRoom room = new BuildingRoom();
                room.setId(gobalInterface.generateId());
                room.setBuildingLevelId(saveBuildingInfoDto.getId());
                room.setBuildingRoom(saveBuildingInfoDto.getName());
                room.setIsDelete(0);
                room.setState(0);
                room.setCreateUser(loginAuthDto.getUserId());
                room.setCreateTime(new Date());
                roomMapper.insert(room);
                id = room.getId();
            } else if (4 == saveBuildingInfoDto.getType()) {
                BuildingBed bed = new BuildingBed();
                bed.setId(gobalInterface.generateId());
                bed.setBedName(saveBuildingInfoDto.getName());
                bed.setRoomId(saveBuildingInfoDto.getId());
                bedMapper.insert(bed);
                id = bed.getId();
            }
            return WrapMapper.ok(id);
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
                    return WrapMapper.error("删除失败,楼层下面还有其他宿舍信息");
                }
            } else if (3 == type) {
                QueryWrapper<BuildingBed> bedQueryWrapper = new QueryWrapper<>();
                bedQueryWrapper.eq("room_id", id);
                List<BuildingBed> buildingBeds = bedMapper.selectList(bedQueryWrapper);
                if (PublicUtil.isEmpty(buildingBeds)) {
                    roomMapper.deleteById(id);
                    return WrapMapper.ok("删除成功");
                } else {
                    return WrapMapper.error("删除失败,宿舍下面还有其他房间信息");
                }
            } else if (4 == type) {
                bedMapper.deleteById(id);
                return WrapMapper.ok("删除成功");
            }
        }
        return WrapMapper.error("参数有误");
    }

    @Override
    public PageWrapper<List<BuildingManagerVo>> managerList(Long id, Long masterId, BaseQueryDto baseQueryDto) {
        if (null != id) {
            List<BuildingManagerVo> list = new ArrayList<>();
            BuildingNo buildingNo = noMapper.selectById(id);
            QueryWrapper<BuildingLevel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("building_no_id", id);
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
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
            return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        } else {
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<BuildingManagerDto> buildingManagerDtos = noMapper.selectNoByMasterId(masterId);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(buildingManagerDtos)) {
                List<BuildingManagerVo> list = new ArrayList<>();
                buildingManagerDtos.forEach(dto -> {
                    BuildingManagerVo buildingManagerVo = new BuildingManagerVo();
                    buildingManagerVo.setBuildingLevelId(dto.getLevelId());
                    buildingManagerVo.setBuildingNo(dto.getBuildingNo());
                    buildingManagerVo.setBuildingLevel(dto.getBuildingLevel());
                    SchoolTeacher teacher = teacherService.getTeacher(dto.getTId());
                    if (PublicUtil.isNotEmpty(teacher)) {
                        buildingManagerVo.setManagerId(teacher.getId());
                        buildingManagerVo.setManagerName(teacher.getTName());
                        buildingManagerVo.setManagerNo(teacher.getTNumber());
                        buildingManagerVo.setManagerPhone(teacher.getPhone());
                    }
                    list.add(buildingManagerVo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        }
        return PageWrapMapper.wrap(200, "暂无数据");
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
                    if(null != student.getClassId() && null != student.getClassInfoId()) {
                        vo.setClassInfo(classService.getClassAndInfo(student.getClassId(),student.getClassInfoId()));
                    }
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
    public Wrapper<List<SchoolClassTeachersVo>> getBuildingTeachers(Long masterId) {
        List<SchoolTeacher> teachers = teacherService.getTeachersToClass(masterId);
        if (PublicUtil.isNotEmpty(teachers)) {
            List<SchoolClassTeachersVo> list = new ArrayList<>();
            teachers.forEach(t -> {
                SchoolClassTeachersVo vo = new SchoolClassTeachersVo();
                vo.setTId(t.getId());
                vo.setTName(t.getTName());
                if (null != t.getSectionId()) {
                    vo.setSectionName(sectionMapper.selectById(t.getSectionId()).getSectionName());
                }
                list.add(vo);
            });
            return WrapMapper.ok(list);
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
//            SchoolTeacher teacher = teacherService.getTeacher(level.getTId());
//            if (PublicUtil.isNotEmpty(teacher)) {
//                vo.setTName(teacher.getTName());
//            }
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
        return noMapper.checkBuildingInfo(masterId, buildingNo, buildingLevel, buildingRoom, buildingBed);
    }

    @Override
    public PageWrapper<List<BuildingStudentListVo>> studentList(Integer type, Long id, Long masterId, BaseQueryDto baseQueryDto) {
        if (3 == type) {
            QueryWrapper<BuildingBed> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", id);
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<BuildingBed> beds = bedMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(beds)) {
                List<BuildingStudentListVo> list = new ArrayList<>();
                beds.forEach(bed -> {
                    BuildingStudent s = buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("bed_id", bed.getId()));
                    BuildingStudentListVo listVo = new BuildingStudentListVo();
                    if (PublicUtil.isNotEmpty(s)) {
                        listVo.setId(s.getId());
                        BuildingStudentListDto buildingStudentListDto = noMapper.checkBuildingInfoByIds(s.getNoId(), s.getLevelId(), s.getRoomId(), s.getBedId());
                        listVo.setBedNo(buildingStudentListDto.getBedName());
                        listVo.setBedId(s.getBedId());
                        listVo.setBuildingRoom(buildingStudentListDto.getBuildingRoom());
                        listVo.setBuildingLevel(buildingStudentListDto.getBuildingLevel());
                        listVo.setBuildingNo(buildingStudentListDto.getBuildingNo());
                        SchoolStudent student = studentService.selectById(s.getStudentId());
                        listVo.setState(1);
                        listVo.setSName(student.getSName());
                        if (null != student.getSNumber()) {
                            listVo.setSNumber(student.getSNumber());
                        }
                        if(null != student.getClassId() && null != student.getClassInfoId()) {
                            listVo.setClassInfo(classService.getClassAndInfo(student.getClassId(),student.getClassInfoId()));
                        }
                    } else {
                        BuildingBedDto livingInfo = bedMapper.getLivingInfo(bed.getId());
                        listVo.setBedId(bed.getId());
                        listVo.setBedNo(livingInfo.getBedName());
                        listVo.setBuildingRoom(livingInfo.getBuildingRoom());
                        listVo.setBuildingLevel(livingInfo.getBuildingLevel());
                        listVo.setBuildingNo(livingInfo.getBuildingNo());
                        listVo.setState(0);
                    }
                    list.add(listVo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        } else if (2 == type) {
            QueryWrapper<BuildingStudent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("level_id", id);
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<BuildingStudent> buildingStudents = buildingStudentMapper.selectList(queryWrapper);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(buildingStudents)) {
                List<BuildingStudentListVo> list = new ArrayList<>();
                buildingStudents.forEach(s -> {
                    BuildingStudentListVo listVo = new BuildingStudentListVo();
                    listVo.setId(s.getId());
                    BuildingStudentListDto buildingStudentListDto = noMapper.checkBuildingInfoByIds(s.getNoId(), s.getLevelId(), s.getRoomId(), s.getBedId());
                    listVo.setBedNo(buildingStudentListDto.getBedName());
                    listVo.setBuildingRoom(buildingStudentListDto.getBuildingRoom());
                    listVo.setBuildingLevel(buildingStudentListDto.getBuildingLevel());
                    listVo.setBuildingNo(buildingStudentListDto.getBuildingNo());
                    SchoolStudent student = studentService.selectById(s.getStudentId());
                    listVo.setState(1);
                    listVo.setSName(student.getSName());
                    if (null != student.getSNumber()) {
                        listVo.setSNumber(student.getSNumber());
                    }
                    if(null != student.getClassId() && null != student.getClassInfoId()) {
                        listVo.setClassInfo(classService.getClassAndInfo(student.getClassId(),student.getClassInfoId()));
                    }
                    list.add(listVo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        } else if (1 == type) {
            Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            List<BuildingStudent> thisSchoolStudent = buildingStudentMapper.getThisSchoolStudent(masterId);
            Long total = page.getTotal();
            if (PublicUtil.isNotEmpty(thisSchoolStudent)) {
                List<BuildingStudentListVo> list = new ArrayList<>();
                thisSchoolStudent.forEach(s -> {
                    BuildingStudentListVo listVo = new BuildingStudentListVo();
                    listVo.setId(s.getId());
                    BuildingStudentListDto buildingStudentListDto = noMapper.checkBuildingInfoByIds(s.getNoId(), s.getLevelId(), s.getRoomId(), s.getBedId());
                    listVo.setBedNo(buildingStudentListDto.getBedName());
                    listVo.setBuildingRoom(buildingStudentListDto.getBuildingRoom());
                    listVo.setBuildingLevel(buildingStudentListDto.getBuildingLevel());
                    listVo.setBuildingNo(buildingStudentListDto.getBuildingNo());
                    SchoolStudent student = studentService.selectById(s.getStudentId());
                    listVo.setState(1);
                    listVo.setSName(student.getSName());
                    if (null != student.getSNumber()) {
                        listVo.setSNumber(student.getSNumber());
                    }
                    if(null != student.getClassId() && null != student.getClassInfoId()) {
                        listVo.setClassInfo(classService.getClassAndInfo(student.getClassId(),student.getClassInfoId()));
                    }
                    list.add(listVo);
                });
                return PageWrapMapper.wrap(list, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
            }
        }
        return PageWrapMapper.wrap(200, "暂无数据");
    }

    @Override
    public Wrapper editBuildingTree(Integer type, Long id, String name) {
        if (1 == type) {
            BuildingNo buildingNo = noMapper.selectById(id);
            buildingNo.setBuildingNo(name);
            noMapper.updateById(buildingNo);
        } else if (2 == type) {
            BuildingLevel buildingLevel = levelMapper.selectById(id);
            buildingLevel.setBuildingLevel(name);
            levelMapper.updateById(buildingLevel);
        } else if (3 == type) {
            BuildingRoom buildingRoom = roomMapper.selectById(id);
            buildingRoom.setBuildingRoom(name);
            roomMapper.updateById(buildingRoom);
        } else if (4 == type) {
            BuildingBed buildingBed = bedMapper.selectById(id);
            buildingBed.setBedName(name);
            bedMapper.updateById(buildingBed);
        }
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper deleteBuildingStudent(Long sId) {
        if (null != sId) {
            buildingStudentMapper.deleteById(sId);
            return WrapMapper.ok("删除成功");
        }
        return null;
    }

    @Override
    public List<SchoolStudentBuildingVo> getStudentBuildingInfo(Long masterId, Integer type, Long id) {
        List<SchoolStudentBuildingVo> list = new ArrayList<>();
        if (null == type && null == id) {
            List<BuildingNo> nos = noMapper.selectList(new QueryWrapper<BuildingNo>().eq("master_id", masterId));
            if (PublicUtil.isNotEmpty(nos)) {
                nos.forEach(no -> {
                    SchoolStudentBuildingVo vo = new SchoolStudentBuildingVo();
                    vo.setId(no.getId());
                    vo.setType(1);
                    vo.setName(no.getBuildingNo());
                    list.add(vo);
                });
                return list;
            }
        } else if (2 == type) {
            List<BuildingLevel> levels = levelMapper.selectList(new QueryWrapper<BuildingLevel>().eq("building_no_id", id));
            if (PublicUtil.isNotEmpty(levels)) {
                levels.forEach(level -> {
                    SchoolStudentBuildingVo vo = new SchoolStudentBuildingVo();
                    vo.setId(level.getId());
                    vo.setType(2);
                    vo.setName(level.getBuildingLevel());
                    list.add(vo);
                });
                return list;
            }
        } else if (3 == type) {
            List<BuildingRoom> rooms = roomMapper.selectList(new QueryWrapper<BuildingRoom>().eq("building_level_id", id));
            if (PublicUtil.isNotEmpty(rooms)) {
                rooms.forEach(room -> {
                    SchoolStudentBuildingVo vo = new SchoolStudentBuildingVo();
                    vo.setId(room.getId());
                    vo.setType(3);
                    vo.setName(room.getBuildingRoom());
                    list.add(vo);
                });
                return list;
            }
        }else {
                List<BuildingBed> beds = bedMapper.selectList(new QueryWrapper<BuildingBed>().eq("room_id", id));
                if (PublicUtil.isNotEmpty(beds)) {
                    beds.forEach(bed -> {
                        SchoolStudentBuildingVo vo = new SchoolStudentBuildingVo();
                        if (null == buildingStudentMapper.selectOne(new QueryWrapper<BuildingStudent>().eq("bed_id", bed.getId()))) {
                            vo.setState(0);
                        } else {
                            vo.setState(1);
                        }
                        vo.setId(bed.getId());
                        vo.setType(4);
                        vo.setName(bed.getBedName());
                        list.add(vo);
                    });
                    return list;
                }
            }
        return null;
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
                buildingNo.setType(1);
                buildingNo.setBuildingNoName(no.getBuildingNo());
                buildingNo.setBuildingLevels(getLevels(no.getId()));
                list.add(buildingNo);
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.ok();
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
                buildingLevel.setType(2);
                buildingLevel.setBuildingLevelName(level.getBuildingLevel());
                buildingLevel.setBuildingRooms(getRooms(level.getId()));
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
                buildingRoom.setType(3);
                buildingRoom.setBuildingRoomName(room.getBuildingRoom());
                buildingRoom.setBuildingBeds(getBeds(room.getId()));
                list.add(buildingRoom);
            });
            return list;
        }
        return null;
    }

    // 4:第四层
    private List<BuildingTreeVo.BuildingLevel.BuildingRoom.BuildingBed> getBeds(Long id) {
        QueryWrapper<BuildingBed> bedQueryWrapper = new QueryWrapper<>();
        bedQueryWrapper.eq("room_id", id);
        List<BuildingBed> buildingBeds = bedMapper.selectList(bedQueryWrapper);
        List<BuildingTreeVo.BuildingLevel.BuildingRoom.BuildingBed> list = new ArrayList<>();
        if (PublicUtil.isNotEmpty(buildingBeds)) {
            buildingBeds.forEach(bed -> {
                BuildingTreeVo.BuildingLevel.BuildingRoom.BuildingBed buildingBed = new BuildingTreeVo.BuildingLevel.BuildingRoom.BuildingBed();
                buildingBed.setBedId(bed.getId());
                buildingBed.setType(4);
                buildingBed.setBedName(bed.getBedName());
                list.add(buildingBed);
            });
            return list;
        }
        return null;
    }
}
