package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.BaseQueryDto;
import com.safe.campus.about.utils.wrapper.PageWrapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SysRole;
import com.safe.campus.model.vo.SysRoleVo;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
public interface SysRoleService extends IService<SysRole> {

    Wrapper saveRole(Long masterId,String roleName, String description, LoginAuthDto loginAuthDto);

    Wrapper deleteRole(Long id, LoginAuthDto loginAuthDto);

    Wrapper editRole(Long id, String roleName, String description,LoginAuthDto loginAuthDto);

    Wrapper getRole(Long id, LoginAuthDto loginAuthDto);

    PageWrapper<List<SysRoleVo>> listRole(Long masterId, BaseQueryDto baseQueryDto, LoginAuthDto loginAuthDto);

    Wrapper setRole(Long id, Integer state, LoginAuthDto loginAuthDto);
}
