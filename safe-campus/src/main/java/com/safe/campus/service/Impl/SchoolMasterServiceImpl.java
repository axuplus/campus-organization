package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.Md5Utils;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.StringUtils;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.*;
import com.safe.campus.model.domain.*;
import com.safe.campus.model.dto.SaveOrEditNodeDto;
import com.safe.campus.model.dto.SchoolIntroductionDto;
import com.safe.campus.model.dto.SchoolMasterDto;
import com.safe.campus.model.dto.SchoolMaterConfDto;
import com.safe.campus.model.vo.*;
import com.safe.campus.service.SchoolMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 学校总校区表 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolMasterServiceImpl extends ServiceImpl<SchoolMasterMapper, SchoolMaster> implements SchoolMasterService {

    @Autowired
    private SchoolMasterMapper masterMapper;

    @Autowired
    private SchoolRootMapper rootMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SysAdminUserMapper userMapper;

    @Autowired
    private MasterRouteMapper routeMapper;

    @Autowired
    private RouteConfMapper confMapper;

    @Autowired
    private LocationProvincesMapper provincesMapper;

    @Autowired
    private LocationCitiesMapper citiesMapper;

    @Autowired
    private LocationAreasMapper areasMapper;

    @Override
    public Wrapper saveSchool(LoginAuthDto loginAuthDto, SchoolMasterDto schoolMasterDto) {
        if (1 != userMapper.selectById(loginAuthDto.getUserId()).getType()) {
            return WrapMapper.error("请联系安校管理创建学校");
        }
        if (PublicUtil.isNotEmpty(schoolMasterDto)) {
            SchoolMaster master = new SchoolMaster();
            master.setId(gobalInterface.generateId());
            master.setAddress(schoolMasterDto.getAddress());
            master.setAreaAddress(new Gson().toJson(schoolMasterDto.getCityInfo()));
            master.setAreaName(schoolMasterDto.getSchoolName());
            master.setCreatedTime(new Date());
            master.setCreatedUser(loginAuthDto.getUserId());
            master.setIsDelete(0);
            master.setLogo(schoolMasterDto.getLogo());
            master.setRealPicture(schoolMasterDto.getRealPicture());
            master.setRootId(schoolMasterDto.getRootId());
            master.setServiceTime(new Gson().toJson(schoolMasterDto.getServiceTime()));
            master.setState(0);
            // 账号
            SysAdmin admin = new SysAdmin();
            admin.setId(gobalInterface.generateId());
            admin.setUserName(schoolMasterDto.getAccount());
            admin.setPassword(Md5Utils.md5Str(schoolMasterDto.getPassword()));
            admin.setCreateUser(loginAuthDto.getUserId());
            master.setState(schoolMasterDto.getState());
            admin.setType(2);
            admin.setLevel(2);
            admin.setState(0);
            admin.setMasterId(master.getId());
            admin.setAppKey(StringUtils.getRandomString(7).toUpperCase());
            admin.setAppSecret(StringUtils.getRandomString(13).toLowerCase());
            admin.setCreateTime(new Date());
            masterMapper.insert(master);
            userMapper.insert(admin);
            return WrapMapper.ok("保存成功");
        }
        return WrapMapper.error("参数有误");
    }

    @Override
    public Wrapper getSchool(LoginAuthDto loginAuthDto, Long id) {
        SchoolMaster master = masterMapper.selectById(id);
        QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", id).eq("type", 2);
        SysAdmin admin = userMapper.selectOne(queryWrapper);
        SchoolMasterVo vo = new SchoolMasterVo();
        vo.setId(master.getId());
        vo.setRootId(master.getRootId());
        vo.setLogo(master.getLogo());
        vo.setRealPicture(master.getRealPicture());
        vo.setSchoolName(master.getAreaName());
        vo.setState(master.getState());
        vo.setAddress(master.getAddress());
        SchoolMasterDto.ServiceTime time = new Gson().fromJson(master.getServiceTime(), SchoolMasterDto.ServiceTime.class);
        SchoolMasterDto.ServiceTime serviceTime = new SchoolMasterDto.ServiceTime();
        serviceTime.setStartTime(time.getStartTime());
        serviceTime.setEndTime(time.getEndTime());
        vo.setServiceTime(serviceTime);
        SchoolMasterVo.CityInfo cityInfo = new Gson().fromJson(master.getAreaAddress(), SchoolMasterVo.CityInfo.class);
        SchoolMasterVo.CityInfo info = new SchoolMasterVo.CityInfo();
        info.setProvince(provincesMapper.selectOne(new QueryWrapper<LocationProvinces>().eq("provinceid", cityInfo.getProvince())).getProvince());
        info.setCity(citiesMapper.selectOne(new QueryWrapper<LocationCities>().eq("cityid", cityInfo.getCity())).getCity());
        info.setAreas(areasMapper.selectOne(new QueryWrapper<LocationAreas>().eq("areaid", cityInfo.getAreas())).getArea());
        info.setProvinceId(cityInfo.getProvince());
        info.setCityId(cityInfo.getCity());
        info.setAreasId(cityInfo.getAreas());
        vo.setCityInfo(info);
        vo.setAdminId(admin.getId());
        vo.setAccount(admin.getUserName());
        vo.setAppKey(admin.getAppKey());
        vo.setAppSecret(admin.getAppSecret());
        return WrapMapper.ok(vo);
    }

    @Override
    public Wrapper editSchool(LoginAuthDto loginAuthDto, SchoolMasterDto schoolMasterDto) {
        if (PublicUtil.isNotEmpty(schoolMasterDto)) {
            SchoolMaster master = new SchoolMaster();
            master.setId(schoolMasterDto.getId());
            master.setAddress(schoolMasterDto.getAddress());
            master.setAreaAddress(new Gson().toJson(schoolMasterDto.getCityInfo()));
            master.setAreaName(schoolMasterDto.getSchoolName());
            master.setLogo(schoolMasterDto.getLogo());
            master.setRealPicture(schoolMasterDto.getRealPicture());
            master.setRootId(schoolMasterDto.getRootId());
            master.setState(schoolMasterDto.getState());
            masterMapper.updateById(master);
        }
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper listSchool(LoginAuthDto loginAuthDto) {
        List<SchoolMaster> masters = masterMapper.getSchoolMater();
        if (PublicUtil.isNotEmpty(masters)) {
            List<SchoolMasterListVo> vos = new ArrayList<>();
            for (SchoolMaster master : masters) {
                SchoolMasterListVo vo = new SchoolMasterListVo();
                vo.setId(master.getId());
                vo.setSchoolName(master.getAreaName());
                SchoolMasterDto.ServiceTime serviceTime = new Gson().fromJson(master.getServiceTime(), SchoolMasterDto.ServiceTime.class);
                vo.setEndTime(serviceTime.getEndTime());
                vo.setState(master.getState());
                QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("create_user", loginAuthDto.getUserId()).eq("master_id", master.getId());
                SysAdmin admin = userMapper.selectOne(queryWrapper);
                vo.setAccount(admin.getUserName());
                vo.setAppKey(admin.getAppKey());
                vos.add(vo);
            }
            return WrapMapper.ok(vos);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper deleteNode(LoginAuthDto loginAuthDto, Long rootId) {
        if (null != rootId) {
            if (null == rootMapper.checkSub(rootId)) {
                rootMapper.deleteById(rootId);
                return WrapMapper.ok("删除成功");
            } else {
                return WrapMapper.error("请先处理下面节点");
            }
        }
        return WrapMapper.error("ID不能为空");
    }

    @Override
    public Wrapper listNode(LoginAuthDto loginAuthDto) {
        List<SchoolRootTreeVo> list = rootMapper.findAll();
        List<SchoolRootTreeVo> sysDepts = new ArrayList<>();
        for (SchoolRootTreeVo dept : list) {
            if (dept.getPid() == null || dept.getPid() == 0) {
                dept.setLevel(0);
                sysDepts.add(dept);
            }
        }
        findChildren(sysDepts, list);
        return WrapMapper.ok(sysDepts);
    }

    @Override
    public Wrapper saveConf(LoginAuthDto loginAuthDto, SchoolMaterConfDto schoolMaterConfDto) {
        if (1 != userMapper.selectById(loginAuthDto.getUserId()).getType()) {
            return WrapMapper.error("请联系安校管理添加");
        }
        if (PublicUtil.isNotEmpty(schoolMaterConfDto)) {
            QueryWrapper<MasterRoute> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("master_id", schoolMaterConfDto.getMasterId());
            routeMapper.delete(queryWrapper);
            schoolMaterConfDto.getRouteId().forEach(r -> {
                MasterRoute masterRoute = new MasterRoute();
                masterRoute.setId(gobalInterface.generateId());
                masterRoute.setRouteId(r);
                masterRoute.setMasterId(schoolMaterConfDto.getMasterId());
                routeMapper.insert(masterRoute);
            });
            return WrapMapper.ok("保存成功");
        }
        return WrapMapper.error("参数不能为空");
    }

    @Override
    public Wrapper listProvince(LoginAuthDto loginAuthDto) {
        return WrapMapper.ok(provincesMapper.getPovinces());
    }

    @Override
    public Wrapper listCity(LoginAuthDto loginAuthDto, Long provinceId) {
        QueryWrapper<LocationCities> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provinceid", provinceId);
        return WrapMapper.ok(citiesMapper.selectList(queryWrapper));
    }

    @Override
    public Wrapper listArea(LoginAuthDto loginAuthDto, Long cityId) {
        QueryWrapper<LocationAreas> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cityid", cityId);
        return WrapMapper.ok(areasMapper.selectList(queryWrapper));
    }

    @Override
    public Wrapper resetPassword(LoginAuthDto loginAuthDto, String account) {
        if (null != account) {
            QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_name", account);
            SysAdmin admin = userMapper.selectOne(queryWrapper);
            String randomString = StringUtils.getRandomString(7);
            admin.setPassword(Md5Utils.md5Str(randomString));
            userMapper.updateById(admin);
            return WrapMapper.ok(randomString);
        }
        return WrapMapper.error("参数不能为空");
    }

    @Override
    public Wrapper listSchoolConf(LoginAuthDto loginAuthDto, Long masterId) {
        QueryWrapper<MasterRoute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("master_id", masterId);
        List<MasterRoute> routes = routeMapper.selectList(queryWrapper);
        ListConfigVo listConfigVo = new ListConfigVo();
        listConfigVo.setSchoolName(masterMapper.selectById(masterId).getAreaName());
        List<ListConfigVo.Modules> list = new ArrayList<>();
        if (PublicUtil.isNotEmpty(routes)) {
            List<Long> ids = routes.stream().map(MasterRoute::getRouteId).collect(toList());
            List<RouteConf> allRoutes = confMapper.getAllRoutes();
            Map<String, List<RouteConf>> map = allRoutes.stream().collect(Collectors.groupingBy(RouteConf::getName));
            List<String> names = map.keySet().stream().collect(toList());
            names.forEach(name -> {
                ListConfigVo.Modules configVo = new ListConfigVo.Modules();
                List<ListConfigVo.Modules.SubInfo> subInfos = new ArrayList<>();
                configVo.setModuleName(name);
                allRoutes.forEach(r -> {
                    if (r.getName().equals(name)) {
                        ListConfigVo.Modules.SubInfo subInfo = new ListConfigVo.Modules.SubInfo();
                        subInfo.setId(r.getId());
                        subInfo.setSubName(r.getSubName());
                        if (ids.contains(r.getId())) {
                            subInfo.setState(1);
                        } else {
                            subInfo.setState(0);
                        }
                        subInfos.add(subInfo);
                    }
                });
                configVo.setSubInfo(subInfos);
                list.add(configVo);
            });
            listConfigVo.setModules(list);
            return WrapMapper.ok(listConfigVo);
        }
        return WrapMapper.error("暂无数据");
    }

    @Override
    public Wrapper saveOrEditNode(SaveOrEditNodeDto saveOrEditNodeDto) {
        if (PublicUtil.isNotEmpty(saveOrEditNodeDto)) {
            if (1 == saveOrEditNodeDto.getType()) {
                SchoolRoot root = new SchoolRoot();
                root.setId(gobalInterface.generateId());
                root.setPId(saveOrEditNodeDto.getId());
                root.setRootName(saveOrEditNodeDto.getNodeName());
                rootMapper.insert(root);
                return WrapMapper.ok(root.getId());
            } else {
                SchoolRoot schoolRoot = rootMapper.selectById(saveOrEditNodeDto.getId());
                schoolRoot.setRootName(saveOrEditNodeDto.getNodeName());
                rootMapper.updateById(schoolRoot);
                return WrapMapper.ok("修改成功");
            }
        }
        return null;
    }

    @Override
    public Wrapper saveOrEditIntroduction(SchoolIntroductionDto schoolIntroductionDto) {
        if (PublicUtil.isNotEmpty(schoolIntroductionDto)) {
            SchoolMaster schoolMaster = masterMapper.selectById(schoolIntroductionDto.getMasterId());
            schoolMaster.setDescription(schoolIntroductionDto.getIntroduction());
            schoolMaster.setImgs(schoolIntroductionDto.getImgs().toString());
            masterMapper.updateById(schoolMaster);
            return WrapMapper.ok("操作成功");
        }
        return null;
    }

    @Override
    public Wrapper<SchoolIntroductionDto> getIntroduction(Long masterId) {
        SchoolMaster schoolMaster = masterMapper.selectById(masterId);
        if (null != schoolMaster.getDescription() && null != schoolMaster.getImgs()) {
            SchoolIntroductionDto dto = new SchoolIntroductionDto();
            dto.setMasterId(masterId);
            dto.setIntroduction(schoolMaster.getDescription());
            dto.setImgs(Arrays.asList(schoolMaster.getImgs()));
            return WrapMapper.ok(dto);
        }
        return null;
    }

    private void findChildren(List<SchoolRootTreeVo> sysDepts, List<SchoolRootTreeVo> depts) {
        for (SchoolRootTreeVo sysDept : sysDepts) {
            List<SchoolRootTreeVo> children = new ArrayList<>();
            for (SchoolRootTreeVo dept : depts) {
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
}

