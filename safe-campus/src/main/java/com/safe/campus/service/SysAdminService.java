package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SysAdmin;

/**
 * <p>
 * 管理员 服务类
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
public interface SysAdminService extends IService<SysAdmin> {

    Wrapper saveAdminUser(String userName, String password, LoginAuthDto loginAuthDto);

    boolean havePermission(Long userId,String url,String type);

    Wrapper deleteAdminUser(Long userId, LoginAuthDto loginAuthDto);

    Wrapper editAdminUser(Long userId, String userName, String password, LoginAuthDto loginAuthDto);

    Wrapper getAdminUser(Long userId, LoginAuthDto loginAuthDto);

    Wrapper listAdminUser(LoginAuthDto loginAuthDto);

}
