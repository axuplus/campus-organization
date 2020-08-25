package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.model.domain.SysAdmin;
import com.safe.campus.model.vo.LoginTokenVo;

public interface TokenService extends IService<SysAdmin> {

    LoginTokenVo token(final String account, final String pwd);

    Boolean exit(final Long userId);

}
