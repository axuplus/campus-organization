package com.safe.campus.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.safe.campus.about.utils.PublicUtil;
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
import org.apache.catalina.mbeans.RoleMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Wrapper<MenuListVo> listMenu(Long roleId) {
        MenuListVo menuListVo = new MenuListVo();
        menuListVo.setName("基本设置");
        MenuListVo.RoleIfo roleInfo = new MenuListVo.RoleIfo();
        roleInfo.setName("组织管理");
        List<SysModule> modules = moduleMapper.getList();
        List<MenuListVo.RoleIfo.ModuleInfo> moduleInfos = new ArrayList<>();
        for (SysModule module : modules) {
            MenuListVo.RoleIfo.ModuleInfo  moduleInfo = new MenuListVo.RoleIfo.ModuleInfo();
            moduleInfo.setModuleName(module.getModuleName());
            QueryWrapper<SysMenu> menuQueryWrapper = new QueryWrapper<>();
            menuQueryWrapper.eq("module_id", module.getId());
            List<SysMenu> menus = menuMapper.selectList(menuQueryWrapper);
            List<MenuListVo.RoleIfo.ModuleInfo.MenuInfo> menuInfos = new ArrayList<>();
            for (SysMenu menu : menus) {
                MenuListVo.RoleIfo.ModuleInfo.MenuInfo info = new MenuListVo.RoleIfo.ModuleInfo.MenuInfo();
                SysRoleMenu sysRoleMenu = roleMenuMapper.selectOne(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId).eq("menu_id", menu.getId()));
                if (PublicUtil.isNotEmpty(sysRoleMenu)) {
                    info.setChecked(true);
                } else {
                    info.setChecked(false);
                }
                info.setId(menu.getId());
                info.setType(menu.getType());
                info.setDescription(menu.getDescription());
                menuInfos.add(info);
            }
            moduleInfo.setMenuInfo(menuInfos);
            moduleInfos.add(moduleInfo);
        }
        roleInfo.setModuleInfo(moduleInfos);
        menuListVo.setRoleIfo(roleInfo);
        return WrapMapper.ok(menuListVo);
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
