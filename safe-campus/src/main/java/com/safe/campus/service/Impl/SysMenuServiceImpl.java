package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.mapper.SysMenuMapper;
import com.safe.campus.mapper.SysModuleMapper;
import com.safe.campus.mapper.SysRoleMenuMapper;
import com.safe.campus.model.domain.SysMenu;
import com.safe.campus.model.domain.SysModule;
import com.safe.campus.model.domain.SysRoleMenu;
import com.safe.campus.model.vo.MenuListVo;
import com.safe.campus.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限菜单 服务实现类
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysModuleMapper moduleMapper;

    @Autowired
    private GobalInterface gobalInterface;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;


    @Override
    public Wrapper listMenu() {
        List<SysModule> modules = moduleMapper.getList();
        List<MenuListVo> vos = new ArrayList<>();
        for (SysModule module : modules) {
            QueryWrapper<SysMenu> menuQueryWrapper = new QueryWrapper<>();
            menuQueryWrapper.eq("module_id", module.getId());
            List<SysMenu> menus = menuMapper.selectList(menuQueryWrapper);
            menus.forEach(m -> {
                MenuListVo menuListVo = new MenuListVo();
                menuListVo.setModuleName(module.getModuleName());
                menuListVo.setModuleName(module.getModuleName());
                MenuListVo.menuInfo menuInfo = new MenuListVo.menuInfo();
                menuInfo.setId(m.getId());
                menuInfo.setType(m.getType());
                menuInfo.setDescription(m.getDescription());
                menuListVo.setMenuInfo(menuInfo);
                vos.add(menuListVo);
            });
        }
        return WrapMapper.ok(vos);
    }

    @Override
    public Wrapper setRoleMenu(Long roleId, List<Long> menuIds) {
        if (null != roleId && null != menuIds) {
            menuIds.forEach(m -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setId(gobalInterface.generateId());
                roleMenu.setMenuId(m);
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
            });
        }
        return WrapMapper.ok("设置成功");
    }
}
