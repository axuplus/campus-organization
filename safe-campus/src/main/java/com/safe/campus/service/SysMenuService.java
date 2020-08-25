package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    Wrapper listMenu();

    Wrapper setRoleMenu(Long roleId, List<Long> menuIds);
}
