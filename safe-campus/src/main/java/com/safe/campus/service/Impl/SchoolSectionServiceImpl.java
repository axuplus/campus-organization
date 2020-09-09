package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.*;
import com.safe.campus.mapper.SchoolMasterMapper;
import com.safe.campus.mapper.SchoolSectionMapper;
import com.safe.campus.mapper.SysAdminUserMapper;
import com.safe.campus.model.domain.SchoolMaster;
import com.safe.campus.model.domain.SchoolSection;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.domain.SysAdmin;
import com.safe.campus.model.dto.SchoolSectionDto;
import com.safe.campus.model.dto.SchoolSectionInfoDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.SchoolSectionService;
import com.safe.campus.service.SchoolTeacherService;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.service.GobalInterface;
import lombok.extern.slf4j.Slf4j;
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
 * 学校部门 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-07-28
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SchoolSectionServiceImpl extends ServiceImpl<SchoolSectionMapper, SchoolSection> implements SchoolSectionService {

    @Autowired
    private SchoolSectionMapper schoolSectionMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SchoolTeacherService teacherService;

    @Autowired
    private SchoolMasterMapper masterMapper;

    @Autowired
    private SysAdminUserMapper adminUserMapper;

    @Override
    public Wrapper saveSchoolSection(SchoolSectionDto schoolSectionDto) {
        if (PublicUtil.isEmpty(schoolSectionDto)) {
            return WrapMapper.error("部门信息不能为空");
        }
        SchoolSection section = new SchoolSection();
        // 根节点
        if (1 == schoolSectionDto.getIsRoot()) {
            section.setLevel(1);
            // 根节点的id就变成子节点的pid
            section.setPId(0L);
        } else {
            section.setPId(schoolSectionDto.getSectionId());
            section.setLevel(schoolSectionMapper.selectById(schoolSectionDto.getSectionId()).getLevel() + 1);
        }
        section.setMasterId(schoolSectionDto.getMasterId());
        section.setId(gobalInterface.generateId());
        section.setSectionName(schoolSectionDto.getName());
        section.setIsDelete(0);
        section.setState(0);
        section.setCreatedTime(new Date());
        if (null != schoolSectionDto.getTId()) {
            section.setTId(schoolSectionDto.getTId());
        }
        int insert = schoolSectionMapper.insert(section);
        if (1 == insert) {
            return WrapMapper.ok(section.getId());
        }
        return null;
    }

    @Override
    public Wrapper editSchoolSection(SchoolSectionInfoDto schoolSectionInfoDto) {
        log.info("========>>>>>>", schoolSectionInfoDto);
        if (PublicUtil.isEmpty(schoolSectionInfoDto)) {
            return WrapMapper.error("部门信息不能为空");
        }
        SchoolSection section = schoolSectionMapper.selectById(schoolSectionInfoDto.getId());
        section.setSectionName(schoolSectionInfoDto.getName());
        if (null != schoolSectionInfoDto.getPid() && 0 != schoolSectionInfoDto.getPid()) {
            section.setPId(schoolSectionInfoDto.getPid());
            section.setLevel(schoolSectionMapper.selectById(schoolSectionInfoDto.getPid()).getLevel() + 1);
        }
        if (null != schoolSectionInfoDto.getTId()) {
            section.setTId(schoolSectionInfoDto.getTId());
        }
        updateById(section);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper getSchoolSection(Long id) {
        if (null == id) {
            return WrapMapper.error("id不能为空");
        }
        SchoolSection schoolSection = schoolSectionMapper.selectById(id);
        if (PublicUtil.isNotEmpty(schoolSection)) {
            SchoolSectionVo vo = new SchoolSectionVo();
            vo.setSectionId(schoolSection.getId());
            vo.setSectionName(schoolSection.getSectionName());
            vo.setPreSectionName(schoolSectionMapper.selectById(schoolSection.getId()).getSectionName());
            SchoolTeacher teacher = teacherService.getTeacherBySection(schoolSection.getTId());
            if (PublicUtil.isNotEmpty(teacher)) {
                vo.setName(teacher.getTName());
                vo.setTId(teacher.getId());
            }
            return WrapMapper.ok(vo);
        }
        return WrapMapper.error("未查到");
    }

    @Override
    public Wrapper deleteSchoolSection(Long id) {
        if (null == id) {
            return WrapMapper.error("id不能为空");
        }
        if (PublicUtil.isNotEmpty(schoolSectionMapper.getSubTrees(id))) {
            return WrapMapper.error("请先处理此部门下面的子部门");
        }
        if (PublicUtil.isNotEmpty(teacherService.getTeacherBySection(id))) {
            return WrapMapper.error("此部门下还有教职工信息");
        }
        int deleteById = schoolSectionMapper.deleteById(id);
        if (1 == deleteById) {
            return WrapMapper.ok("删除成功");
        }
        return WrapMapper.error("删除失败");
    }

    @Override
    public PageWrapper<List<SchoolSectionVo>> listSchoolSection(Long masterId, Integer type, Long id, BaseQueryDto baseQueryDto) {
        if (PublicUtil.isEmpty(masterId)) {
            return PageWrapMapper.wrap(500, "参数不能为空");
        }
        List<SchoolSection> list = null;
        List<Long> subTrees = null;
        Page page = null;
        Long total = null;
        if (0 == type) {
            subTrees = schoolSectionMapper.getSubTrees(id);
            System.out.println("subTrees = " + subTrees);
            if (PublicUtil.isEmpty(subTrees)) {
                return PageWrapMapper.wrap(200, "暂无数据");
            }
            subTrees.add(id);
            page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            list = schoolSectionMapper.selectBatchIds(subTrees);
            total = page.getTotal();
        } else if (1 == type) {
            page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
            list = schoolSectionMapper.selectList(new QueryWrapper<SchoolSection>().eq("master_id", masterId));
            total = page.getTotal();
        } else {
            return null;
        }
        List<SchoolSectionVo> vos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SchoolSection section = list.get(i);
            SchoolSectionVo vo = new SchoolSectionVo();
            vo.setSectionId(section.getId());
            vo.setSectionName(section.getSectionName());
            vo.setState(section.getState());
            SchoolSection pre = null;
            if (0 == i) {
                pre = list.get(i);
            } else {
                pre = list.get(i - 1);
            }
            vo.setPreSectionName(pre.getSectionName());
            if (PublicUtil.isNotEmpty(section.getTId())) {
                SchoolTeacher teacherBySection = teacherService.getTeacherBySection(section.getTId());
                vo.setTId(teacherBySection.getId());
                vo.setName(teacherBySection.getTName());
                vo.setPhone(teacherBySection.getPhone());
            }
            vos.add(vo);
        }
        return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
    }

    @Override
    public Wrapper getDetailsSection(Long id) {
        if (null == id) {
            return WrapMapper.error("id不能为空");
        }
        List<SectionTreeVo> list = schoolSectionMapper.findAll(id);
        List<SectionTreeVo> sysDepts = new ArrayList<>();
        for (SectionTreeVo dept : list) {
            if (dept.getPid() == null || dept.getPid() == 0) {
                dept.setLevel(0);
                sysDepts.add(dept);
            }
        }
        findChildren(sysDepts, list);
        return WrapMapper.ok(sysDepts);
    }

    private void findChildren(List<SectionTreeVo> sysDepts, List<SectionTreeVo> depts) {
        for (SectionTreeVo sysDept : sysDepts) {
            List<SectionTreeVo> children = new ArrayList<>();
            for (SectionTreeVo dept : depts) {
                if (sysDept.getId() != null && sysDept.getId().equals(dept.getPid())) {
                    // dept.setParentName(dept.getSectionName());
                    dept.setLevel(sysDept.getLevel() + 1);
                    children.add(dept);
                }
            }
            sysDept.setChildren(children);
            findChildren(children, depts);
        }
    }


    @Override
    public PageWrapper<List<SchoolSectionVo>> searchSchoolSection(Long masterId, String name, BaseQueryDto baseQueryDto) {
        if (null == name) {
            return PageWrapMapper.wrap(200, "搜索参数不能为空");
        }
        QueryWrapper<SchoolSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("section_name", name);
        Page page = PageHelper.startPage(baseQueryDto.getPage(), baseQueryDto.getPage_size());
        List<SchoolSection> list = schoolSectionMapper.selectList(queryWrapper);
        Long total = page.getTotal();
        if (PublicUtil.isNotEmpty(list)) {
            List<SchoolSectionVo> vos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SchoolSection section = list.get(i);
                SchoolSectionVo vo = new SchoolSectionVo();
                vo.setSectionId(section.getId());
                vo.setSectionName(section.getSectionName());
                vo.setState(section.getState());
                SchoolSection pre = null;
                if (0 == i) {
                    pre = list.get(i);
                } else {
                    pre = list.get(i - 1);
                }
                vo.setPreSectionName(pre.getSectionName());
                SchoolTeacher teacherBySection = teacherService.getTeacherBySection(section.getId());
                if (PublicUtil.isNotEmpty(teacherBySection)) {
                    vo.setTId(teacherBySection.getId());
                    vo.setName(teacherBySection.getTName());
                    vo.setPhone(teacherBySection.getPhone());
                }
                vos.add(vo);
            }
            return PageWrapMapper.wrap(vos, new PageUtil(total.intValue(), baseQueryDto.getPage(), baseQueryDto.getPage_size()));
        }
        return PageWrapMapper.wrap(200, "未查找到结果");
    }

    @Override
    public Wrapper<List<SectionTeachersVo>> getCharge(Long masterId) {
        List<SchoolTeacher> charge = teacherService.getCharge(masterId);
        if (PublicUtil.isNotEmpty(charge)) {
            List<SectionTeachersVo> list = new ArrayList<>();
            charge.forEach(c -> {
                if (null != adminUserMapper.selectOne(new QueryWrapper<SysAdmin>().eq("t_id", c.getId()).eq("state", 0))) {
                    SectionTeachersVo teachersVo = new SectionTeachersVo();
                    teachersVo.setTId(c.getId());
                    teachersVo.setTName(c.getTName());
                    teachersVo.setSectionName(schoolSectionMapper.selectById(c.getSectionId()).getSectionName());
                    list.add(teachersVo);
                }
            });
            return WrapMapper.ok(list);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<Map<Long, String>> getSchools(Long masterId, LoginAuthDto loginAuthDto) {
        QueryWrapper<SchoolMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0).eq("id", masterId);
        List<SchoolMaster> masters = masterMapper.selectList(queryWrapper);
        if (PublicUtil.isNotEmpty(masters)) {
            return WrapMapper.ok(masters.stream().collect(Collectors.toMap(SchoolMaster::getId, SchoolMaster::getAreaName)));
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper<List<SectionVo>> getSuperior(Integer type, Long masterId, Long sectionId) {
        if (1 == type) {
            QueryWrapper<SchoolSection> sectionQueryWrapper = new QueryWrapper<>();
            sectionQueryWrapper.eq("master_id", masterId).eq("level", 1).eq("p_id",0);
            List<SchoolSection> schoolSections = schoolSectionMapper.selectList(sectionQueryWrapper);
            if (PublicUtil.isNotEmpty(schoolSections)) {
                List<SectionVo> vos = new ArrayList<>();
                schoolSections.forEach(s -> {
                    SectionVo sectionVo = new SectionVo();
                    sectionVo.setSectionId(s.getId());
                    sectionVo.setSectionName(s.getSectionName());
                    vos.add(sectionVo);
                });
                return WrapMapper.ok(vos);
            }
        } else {
            List<SchoolSection> schoolSections = schoolSectionMapper.selectList(new QueryWrapper<SchoolSection>().eq("p_id", sectionId).eq("master_id", masterId));
            if (PublicUtil.isNotEmpty(schoolSections)) {
                List<SectionVo> vos = new ArrayList<>();
                schoolSections.forEach(s -> {
                    SectionVo sectionVo = new SectionVo();
                    sectionVo.setSectionId(s.getId());
                    sectionVo.setSectionName(s.getSectionName());
                    vos.add(sectionVo);
                });
                return WrapMapper.ok(vos);
            }
        }
        return WrapMapper.error("暂无下级部门");
    }
}

