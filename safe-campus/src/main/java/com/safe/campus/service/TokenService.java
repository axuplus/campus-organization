package com.safe.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.domain.SysAdmin;
import com.safe.campus.model.vo.LoginTokenVo;
import com.safe.campus.model.vo.ModuleConfVo;

public interface TokenService extends IService<SysAdmin> {

    LoginTokenVo token(final String account, final String pwd);

    Boolean exit(final Long userId);

    Wrapper<ModuleConfVo> getModuleConf(Long userId);

    Wrapper checkAdmin(Long userId);
}
