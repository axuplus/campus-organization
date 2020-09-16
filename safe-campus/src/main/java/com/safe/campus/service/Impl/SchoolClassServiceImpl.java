package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.mapper.SchoolClassInfoMapper;
import com.safe.campus.mapper.SchoolClassMapper;
import com.safe.campus.mapper.SchoolSectionMapper;
import com.safe.campus.model.domain.SchoolClass;
import com.safe.campus.model.domain.SchoolClassInfo;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.dto.SchoolClassDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.SchoolClassService;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.service.GobalInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学校年级表 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-07-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolClassServiceImpl extends ServiceImpl<SchoolClassMapper, SchoolClass> implements SchoolClassService {


    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SchoolClassMapper schoolClassMapper;

    @Autowired
    private SchoolClassInfoMapper schoolClassInfoMapper;

    @Autowired
    private SchoolTeacherService teacherService;

    @Autowired
    private SchoolSectionMapper sectionMapper;


    @Override
    public Wrapper saveSchoolClass(SchoolClassDto schoolClassDto, LoginAuthDto loginAuthDto) {
        if (PublicUtil.isEmpty(schoolClassDto)) {
            return WrapMapper.error("参数不能为空");
        }
        if (1 == schoolClassDto.getType()) {
            SchoolClass schoolClass = new SchoolClass();
            schoolClass.setId(gobalInterface.generateId());
            schoolClass.setClassName(schoolClassDto.getName());
            schoolClass.setIsDelete(0);
            schoolClass.setMasterId(schoolClassDto.getMasterId());
            schoolClass.setState(0);
            schoolClass.setCreatedUser(loginAuthDto.getUserId());
            schoolClass.setCreatedTime(new Date());
            schoolClassMapper.insert(schoolClass);
            return WrapMapper.ok(schoolClass.getId());
        } else {
            SchoolClassInfo info = new SchoolClassInfo();
            info.setId(gobalInterface.generateId());
            info.setClassId(schoolClassDto.getClassId());
            info.setClassInfoName(schoolClassDto.getName());
            info.setIsDelete(0);
            info.setState(0);
            info.setCreateUser(loginAuthDto.getUserId());
            info.setCreateTime(new Date());
            schoolClassInfoMapper.insert(info);
            return WrapMapper.ok(info.getId());
        }
    }

    @Override
    public Wrapper editClass(Long id, Long tId, String name, Integer type, LoginAuthDto loginAuthDto) {
        if (1 == type && null != type) {
            SchoolClass byId = schoolClassMapper.selectById(id);
            if (PublicUtil.isNotEmpty(byId)) {
                if (null != name) {
                    byId.setClassName(name);
                }
                if (null != tId) {
                    byId.setTId(tId);
                }
                schoolClassMapper.updateById(byId);
                return WrapMapper.ok("编辑成功");
            }
        } else {
            SchoolClassInfo classInfo = schoolClassInfoMapper.selectById(id);
            if (PublicUtil.isNotEmpty(classInfo)) {
                if (null != name) {
                    classInfo.setClassInfoName(name);
                }
                if (null != tId) {
                    classInfo.setTId(tId);
                }
                schoolClassInfoMapper.updateById(classInfo);
            }
            return WrapMapper.ok("编辑成功");
        }
        return WrapMapper.error("参数有误");
    }


    @Override
    public Wrapper deleteSchoolClass(Integer type, Long id) {
        if (null != type && null != id) {
            if (1 == type) {
                QueryWrapper<SchoolClassInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("class_id", id).eq("is_delete", 0);
                List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(queryWrapper);
                if (PublicUtil.isNotEmpty(infos)) {
                    return WrapMapper.error("此年级下面还有其他班级");
                }
                schoolClassMapper.deleteById(id);
            } else if (2 == type) {
                schoolClassInfoMapper.deleteById(id);
            }
            return WrapMapper.ok("删除成功");
        }
        return WrapMapper.error("参数不能为空");
    }

    @Override
    public Wrapper operation(Integer type, Integer state, Long id) {
        if (1 == type) {
            if (null != state && null != id) {
                SchoolClass schoolClass = schoolClassMapper.selectById(id);
                if (1 == state) {
                    QueryWrapper<SchoolClassInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("class_id", id).eq("is_delete", 0);
                    List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(queryWrapper);
                    if (PublicUtil.isNotEmpty(infos)) {
                        return WrapMapper.error("此年级下面还有其他班级");
                    } else {
                        schoolClass.setState(1);
                        schoolClassMapper.updateById(schoolClass);
                        return WrapMapper.ok("操作成功");
                    }
                } else {
                    schoolClass.setState(0);
                    schoolClassMapper.updateById(schoolClass);
                    return WrapMapper.ok("操作成功");
                }
            }
        } else if (2 == type) {
            if (null != state && null != id) {
                SchoolClassInfo info = schoolClassInfoMapper.selectById(id);
                if (1 == state) {
                    info.setState(1);
                } else {
                    info.setState(0);
                }
                schoolClassInfoMapper.updateById(info);
                return WrapMapper.ok("操作成功");
            }
        }
        return null;
    }

    @Override
    public String getClassAndInfo(Long classId, Long classInfoId) {
        return schoolClassMapper.selectById(classId).getClassName() + " " + schoolClassInfoMapper.selectById(classInfoId).getClassInfoName();
    }

    @Override
    public Wrapper<List<SchoolClassSearchVo>> searchSchoolClass(Long masterId, String name) {
        if (null == name) {
            return WrapMapper.error("请输入搜索条件");
        }
        if (name.contains("年级")) {
            QueryWrapper<SchoolClass> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("class_name", name).eq("is_delete", 0).eq("master_id", masterId);
            SchoolClass schoolClass = schoolClassMapper.selectOne(queryWrapper);
            List<SchoolClassSearchVo> list = new ArrayList<>();
            if (PublicUtil.isNotEmpty(schoolClass)) {
                SchoolClassSearchVo vo = new SchoolClassSearchVo();
                vo.setClassId(schoolClass.getId());
                vo.setClassName(schoolClass.getClassName());
                vo.setState(schoolClass.getState());
                SchoolTeacher teacher = teacherService.getTeacher(schoolClass.getTId());
                if (PublicUtil.isNotEmpty(teacher)) {
                    vo.setClassSuperiorName(teacher.getTName());
                    vo.setPhone(teacher.getPhone());
                }
                vo.setType(1);
                list.add(vo);
                QueryWrapper<SchoolClassInfo> query = new QueryWrapper<>();
                query.eq("class_id", schoolClass.getId()).eq("is_delete", 0);
                List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(query);
                if (PublicUtil.isNotEmpty(infos)) {
                    infos.forEach(info -> {
                        SchoolClassSearchVo searchVo = new SchoolClassSearchVo();
                        searchVo.setClassInfoId(info.getId());
                        searchVo.setClassInfoName(info.getClassInfoName());
                        searchVo.setType(2);
                        searchVo.setState(info.getState());
                        searchVo.setClassName(schoolClass.getClassName());
                        SchoolTeacher t = teacherService.getTeacher(schoolClass.getTId());
                        if (PublicUtil.isNotEmpty(t)) {
                            searchVo.setClassSuperiorName(t.getTName());
                            searchVo.setPhone(t.getPhone());
                        }
                        list.add(searchVo);
                    });
                }
                return WrapMapper.ok(list);
            }
        } else {
            List<SchoolClassSearchVo> list = new ArrayList<>();
            QueryWrapper<SchoolClassInfo> query = new QueryWrapper<>();
            query.like("class_info_name", name).eq("is_delete", 0);
            List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(query);
            if (PublicUtil.isNotEmpty(infos)) {
                infos.forEach(info -> {
                    SchoolClass schoolClass = schoolClassMapper.selectById(info.getClassId());
                    if (masterId.equals(schoolClass.getMasterId())) {
                        SchoolClassSearchVo searchVo = new SchoolClassSearchVo();
                        searchVo.setClassInfoId(info.getId());
                        searchVo.setClassInfoName(info.getClassInfoName());
                        searchVo.setType(2);
                        searchVo.setState(info.getState());
                        searchVo.setClassName(schoolClass.getClassName());
                        SchoolTeacher t = teacherService.getTeacher(schoolClass.getTId());
                        if (PublicUtil.isNotEmpty(t)) {
                            searchVo.setClassSuperiorName(t.getTName());
                            searchVo.setPhone(t.getPhone());
                        }
                        list.add(searchVo);
                    }
                });
                return WrapMapper.ok(list);
            }
        }
        return WrapMapper.error("暂无数据");
    }


    @Override
    public Wrapper<List<NodeTreeVo>> nodeTreeSchoolClass(Long masterId) {
        QueryWrapper<SchoolClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0).eq("master_id", masterId);
        List<SchoolClass> classes = schoolClassMapper.selectList(queryWrapper);
        List<NodeTreeVo> treeVos = new ArrayList<>();
        if (PublicUtil.isNotEmpty(classes)) {
            for (SchoolClass cl : classes) {
                NodeTreeVo treeVo = new NodeTreeVo();
                treeVo.setClassId(cl.getId());
                treeVo.setType(1);
                treeVo.setClassName(cl.getClassName());
                QueryWrapper<SchoolClassInfo> query = new QueryWrapper<>();
                query.eq("class_id", cl.getId()).eq("is_delete", 0);
                List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(query);
                if (PublicUtil.isNotEmpty(infos)) {
                    ArrayList<NodeTreeVo.SubClass> list = new ArrayList<>();
                    infos.forEach(info -> {
                        NodeTreeVo.SubClass subClass = new NodeTreeVo.SubClass();
                        subClass.setSubClassId(info.getId());
                        subClass.setType(2);
                        subClass.setSubClassName(info.getClassInfoName());
                        list.add(subClass);
                        treeVo.setSubClassInfo(list);
                    });
                }
                treeVos.add(treeVo);
            }
            return WrapMapper.ok(treeVos);
        }
        return null;
    }

    @Override
    public List<SchoolClass> getAllClass(Long masterId) {
        QueryWrapper<SchoolClass> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0).eq("master_id", masterId);
        return schoolClassMapper.selectList(wrapper);
    }

    @Override
    public List<SchoolClassInfo> getAllClassInfo(Long classId) {
        QueryWrapper<SchoolClassInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        return schoolClassInfoMapper.selectList(queryWrapper);
    }

    @Override
    public Wrapper<List<SchoolClassSearchVo>> listClass(Long masterId, Long id, Integer type) {
        if (1 == type) {
            QueryWrapper<SchoolClass> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id).eq("is_delete", 0).eq("master_id", masterId).orderByDesc("created_time");
            SchoolClass schoolClass = schoolClassMapper.selectOne(queryWrapper);
            List<SchoolClassSearchVo> list = new ArrayList<>();
            QueryWrapper<SchoolClassInfo> query = new QueryWrapper<>();
            query.eq("class_id", schoolClass.getId()).eq("is_delete", 0);
            List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(query);
            if (PublicUtil.isNotEmpty(infos)) {
                infos.forEach(info -> {
                    SchoolClassSearchVo searchVo = new SchoolClassSearchVo();
                    searchVo.setClassId(schoolClass.getId());
                    searchVo.setClassName(schoolClass.getClassName());
                    searchVo.setClassInfoId(info.getId());
                    searchVo.setClassInfoName(info.getClassInfoName());
                    searchVo.setType(2);
                    searchVo.setState(info.getState());
                    SchoolTeacher t = teacherService.getTeacher(info.getTId());
                    if (PublicUtil.isNotEmpty(t)) {
                        searchVo.setClassSuperiorName(t.getTName());
                        searchVo.setPhone(t.getPhone());
                    }
                    list.add(searchVo);
                });
                // 所属年级
                SchoolClassSearchVo vo = new SchoolClassSearchVo();
                vo.setClassId(schoolClass.getId());
                vo.setClassName(schoolClass.getClassName());
                vo.setState(schoolClass.getState());
                SchoolTeacher teacher = teacherService.getTeacher(schoolClass.getTId());
                if (PublicUtil.isNotEmpty(teacher)) {
                    vo.setClassSuperiorName(teacher.getTName());
                    vo.setPhone(teacher.getPhone());
                }
                vo.setType(1);
                list.add(vo);
            }
            return WrapMapper.ok(list);
        } else if (2 == type) {
            SchoolClassInfo schoolClassInfo = schoolClassInfoMapper.selectById(id);
            if (PublicUtil.isNotEmpty(schoolClassInfo)) {
                List<SchoolClassSearchVo> list = new ArrayList<>();
                SchoolClassSearchVo searchVo = new SchoolClassSearchVo();
                searchVo.setClassInfoId(schoolClassInfo.getId());
                searchVo.setClassInfoName(schoolClassInfo.getClassInfoName());
                searchVo.setType(2);
                searchVo.setState(schoolClassInfo.getState());
                SchoolClass schoolClass = schoolClassMapper.selectById(schoolClassInfo.getClassId());
                searchVo.setClassId(schoolClass.getId());
                searchVo.setClassName(schoolClass.getClassName());
                SchoolTeacher t = teacherService.getTeacher(schoolClassInfo.getTId());
                if (PublicUtil.isNotEmpty(t)) {
                    searchVo.setClassSuperiorName(t.getTName());
                    searchVo.setPhone(t.getPhone());
                }
                list.add(searchVo);
                return WrapMapper.ok(list);
            }
        } else {
            // 全部
            List<SchoolClass> classes = schoolClassMapper.selectList(new QueryWrapper<SchoolClass>().eq("master_id", masterId).orderByDesc("created_time"));
            List<SchoolClassSearchVo> list = new ArrayList<>();
            for (SchoolClass schoolClass : classes) {
                SchoolClassSearchVo vo = new SchoolClassSearchVo();
                vo.setClassId(schoolClass.getId());
                vo.setClassId(schoolClass.getId());
                vo.setClassName(schoolClass.getClassName());
                vo.setState(schoolClass.getState());
                SchoolTeacher teacher = teacherService.getTeacher(schoolClass.getTId());
                if (PublicUtil.isNotEmpty(teacher)) {
                    vo.setClassSuperiorName(teacher.getTName());
                    vo.setPhone(teacher.getPhone());
                }
                vo.setType(1);
                list.add(vo);
                QueryWrapper<SchoolClassInfo> classInfoQueryWrapper = new QueryWrapper<>();
                classInfoQueryWrapper.eq("class_id", schoolClass.getId());
                List<SchoolClassInfo> infos = schoolClassInfoMapper.selectList(classInfoQueryWrapper);
                if (PublicUtil.isNotEmpty(infos)) {
                    infos.forEach(info -> {
                        SchoolClassSearchVo classInfoVo = new SchoolClassSearchVo();
                        classInfoVo.setClassId(schoolClass.getId());
                        classInfoVo.setClassName(schoolClass.getClassName());
                        classInfoVo.setClassInfoId(info.getId());
                        classInfoVo.setClassInfoName(info.getClassInfoName());
                        classInfoVo.setState(info.getState());
                        SchoolTeacher t = teacherService.getTeacher(info.getTId());
                        if (PublicUtil.isNotEmpty(t)) {
                            classInfoVo.setClassSuperiorName(t.getTName());
                            classInfoVo.setPhone(t.getPhone());
                        }
                        classInfoVo.setType(2);
                        list.add(classInfoVo);
                    });
                }
            }
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<List<SchoolClassTeachersVo>> listTeachers(Long masterId) {
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
    public Wrapper<SchoolClassEditVo> getInfo(Integer type, Long id) {
        if (null != type && null != id) {
            if (1 == type) {
                SchoolClass schoolClass = schoolClassMapper.selectById(id);
                SchoolClassEditVo editVo = new SchoolClassEditVo();
                editVo.setClassId(schoolClass.getId());
                editVo.setClassName(schoolClass.getClassName());
                if (null != schoolClass.getTId()) {
                    SchoolTeacher teacher = teacherService.getTeacher(schoolClass.getTId());
                    if (PublicUtil.isNotEmpty(teacher)) {
                        editVo.setSuperiorId(teacher.getId());
                        editVo.setSuperiorName(teacher.getTName());
                    }
                }
                return WrapMapper.ok(editVo);
            } else {
                SchoolClassInfo classInfo = schoolClassInfoMapper.selectById(id);
                SchoolClassEditVo editVo = new SchoolClassEditVo();
                editVo.setClassId(classInfo.getId());
                editVo.setClassName(classInfo.getClassInfoName());
                if (null != classInfo.getTId()) {
                    SchoolTeacher teacher = teacherService.getTeacher(classInfo.getTId());
                    if (PublicUtil.isNotEmpty(teacher)) {
                        editVo.setSuperiorId(teacher.getId());
                        editVo.setSuperiorName(teacher.getTName());
                    }
                }
                return WrapMapper.ok(editVo);
            }
        }
        return null;
    }
}
