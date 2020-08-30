package com.safe.campus.service.Impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.Md5Utils;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.SysAdminUserMapper;
import com.safe.campus.mapper.SysMenuMapper;
import com.safe.campus.mapper.SysUserRoleMapper;
import com.safe.campus.model.domain.SysAdmin;
import com.safe.campus.model.vo.AdminUserVo;
import com.safe.campus.service.SysAdminService;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAdminServiceImpl extends ServiceImpl<SysAdminUserMapper, SysAdmin> implements SysAdminService {

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SysAdminUserMapper adminUserMapper;


    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public Wrapper saveAdminUser(String userName, String password, LoginAuthDto loginAuthDto) {
        if (null != userName && 0 != userName.length() && null != password && 0 != password.length()) {
            SysAdmin sysAdmin = adminUserMapper.selectById(loginAuthDto.getUserId());
            SysAdmin admin = new SysAdmin();
            admin.setId(gobalInterface.generateId());
            admin.setState(0);
            admin.setCreateTime(new Date());
            admin.setPassword(Md5Utils.md5Str(password));
            admin.setUserName(userName);
            admin.setCreateUser(loginAuthDto.getUserId());
            if (1 == sysAdmin.getType()) {
                // 安校总账号创建用户
                admin.setType(1);
                admin.setMasterId(0L);
            } else {
                // 学校创建用户
                admin.setType(3);
                admin.setMasterId(sysAdmin.getMasterId());
            }
            admin.setLevel(sysAdmin.getLevel() + 1);
            adminUserMapper.insert(admin);
        }
        return WrapMapper.ok("添加成功");
    }

    /**
     * 这是初始化请求头的时候去验证TokenInterceptor里面的注解
     * @param userId
     * @param url
     * @param type
     * @return boolean
     */
    @Override
    public boolean havePermission(Long userId, String url, String type) {
        int t;
        if ("ADD".equals(type)) {
            t = 1;
        } else if ("DEL".equals(type)) {
            t = 2;
        } else if ("EDIT".equals(type)) {
            t = 3;
        } else if ("QUERY".equals(type)) {
            t = 4;
        } else if ("SET".equals(type)) {
            t = 5;
        } else if ("ACTIVE".equals(type)) {
            t = 6;
        } else {
            t = 0;
        }
        // 1为安校总账号 2为学校账号 3为学校子账号
        if (1 == adminUserMapper.selectById(userId).getType() || 2 == adminUserMapper.selectById(userId).getType()) {
            return true;
        }
        if (PublicUtil.isNotEmpty(menuMapper.checkHavePermission(userId, url, t))) {
            return true;
        }
        return false;
    }

    @Override
    public Wrapper deleteAdminUser(Long userId, LoginAuthDto loginAuthDto) {
        if (null != userId) {
            adminUserMapper.deleteById(userId);
            return WrapMapper.ok("删除成功");
        }
        return null;
    }

    @Override
    public Wrapper editAdminUser(Long userId, String userName, String password, LoginAuthDto loginAuthDto) {
        if (null == userId || null == userName || null == password) {
            return WrapMapper.error("参数不能为空");
        }
        SysAdmin admin = adminUserMapper.selectById(userId);
        admin.setUserName(userName);
        admin.setPassword(Md5Utils.md5Str(password));
        adminUserMapper.updateById(admin);
        return WrapMapper.ok("修改成功");
    }

    @Override
    public Wrapper getAdminUser(Long userId, LoginAuthDto loginAuthDto) {
        if (null != userId) {
            SysAdmin admin = adminUserMapper.selectById(userId);
            AdminUserVo map = new ModelMapper().map(admin, AdminUserVo.class);
            map.setParentName(adminUserMapper.selectById(admin.getCreateUser()).getUserName());
            return WrapMapper.ok(map);
        }
        return null;
    }

    @Override
    public Wrapper listAdminUser(LoginAuthDto loginAuthDto) {
        List<Long> ids = adminUserMapper.getAdminUsers(loginAuthDto.getUserId());
        ids.add(loginAuthDto.getUserId());
        List<SysAdmin> sysAdmins = adminUserMapper.selectBatchIds(ids);
        if (PublicUtil.isNotEmpty(sysAdmins)) {
            List<AdminUserVo> vos = new ArrayList<>();
            sysAdmins.forEach(a -> {
                AdminUserVo map = new ModelMapper().map(a, AdminUserVo.class);
                map.setParentName(adminUserMapper.selectById(a.getCreateUser()).getUserName());
                vos.add(map);
            });
            return WrapMapper.ok(vos);
        }
        return null;
    }
}
