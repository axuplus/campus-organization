package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.SysRoleMapper;
import com.safe.campus.model.domain.SysRole;
import com.safe.campus.model.vo.SysRoleVo;
import com.safe.campus.service.SysRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SysRoleMapper roleMapper;


    @Override
    public Wrapper saveRole(String roleName, String description, LoginAuthDto loginAuthDto) {
        if (null != roleName && null != description) {
            SysRole role = new SysRole();
            role.setId(gobalInterface.generateId());
            role.setCreateTime(new Date());
            role.setCreateUser(loginAuthDto.getUserId());
            role.setRoleName(roleName);
            role.setState(0);
            role.setDescription(description);
            roleMapper.insert(role);
            return WrapMapper.ok("保存成功");
        }
        return null;
    }

    @Override
    public Wrapper deleteRole(Long id, LoginAuthDto loginAuthDto) {
        roleMapper.deleteById(id);
        return WrapMapper.ok("删除成功");
    }

    @Override
    public Wrapper editRole(Long id, String roleName, String description, LoginAuthDto loginAuthDto) {
        if (null != id && null != roleName) {
            SysRole sysRole = roleMapper.selectById(id);
            sysRole.setRoleName(roleName);
            sysRole.setDescription(description);
            roleMapper.updateById(sysRole);
            return WrapMapper.ok("修改成功");
        }
        return null;
    }

    @Override
    public Wrapper getRole(Long id, LoginAuthDto loginAuthDto) {
        if (null != id) {
            SysRole sysRole = roleMapper.selectById(id);
            SysRoleVo map = new ModelMapper().map(sysRole, SysRoleVo.class);
            return WrapMapper.ok(map);
        }
        return null;
    }

    @Override
    public Wrapper listRole(LoginAuthDto loginAuthDto) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 0).or().eq("state", 1);
        List<SysRole> sysRoles = roleMapper.selectList(queryWrapper);
        if (PublicUtil.isNotEmpty(sysRoles)) {
            List<SysRoleVo> vos = new ArrayList<>();
            sysRoles.forEach(s -> {
                SysRoleVo map = new ModelMapper().map(s, SysRoleVo.class);
                vos.add(map);
            });
            return WrapMapper.ok(vos);
        }
        return null;
    }

    @Override
    public Wrapper setRole(Long id, Integer state, LoginAuthDto loginAuthDto) {
        SysRole sysRole = roleMapper.selectById(id);
        sysRole.setState(state);
        roleMapper.updateById(sysRole);
        return WrapMapper.ok("修改成功");
    }
}
