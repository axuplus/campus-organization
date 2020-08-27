package com.safe.campus.about.controller;


import com.safe.campus.about.annotation.Permission;
import com.safe.campus.about.constant.GlobalConstant;
import com.safe.campus.about.exception.BizException;
import com.safe.campus.about.utils.PublicUtil;
import com.safe.campus.about.utils.ThreadLocalMap;
import com.safe.campus.about.utils.wrapper.WrapMapper;
import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.about.dto.LoginAuthDto;
import com.safe.campus.enums.ErrorCodeEnum;
import com.safe.campus.model.dto.LoginTokenDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Objects;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected LoginAuthDto getLoginAuthDto() {
        LoginTokenDto loginTokenDto = (LoginTokenDto) ThreadLocalMap.get(GlobalConstant.Sys.TOKEN_AUTH_DTO);
        if (PublicUtil.isEmpty(loginTokenDto)) {
            throw new BizException(ErrorCodeEnum.UAC10011041);
        }
        LoginAuthDto loginAuthDto = new LoginAuthDto();
        loginAuthDto.setUserId(loginTokenDto.getId());
        loginAuthDto.setUserName(loginTokenDto.getUserName());
        loginAuthDto.setType(loginTokenDto.getType());
        return loginAuthDto;
    }


    /**
     * Handle result wrapper.
     *
     * @param <T>    the type parameter
     * @param result the result
     *
     * @return the wrapper
     */
    protected <T> Wrapper<T> handleResult(T result) {
        boolean flag = isFlag(result);

        if (flag) {
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "操作成功", result);
        } else {
            return WrapMapper.wrap(Wrapper.ERROR_CODE, "操作失败", result);
        }
    }

    /**
     * Handle result wrapper.
     *
     * @param <E>      the type parameter
     * @param result   the result
     * @param errorMsg the error msg
     *
     * @return the wrapper
     */
    protected <E> Wrapper<E> handleResult(E result, String errorMsg) {
        boolean flag = isFlag(result);

        if (flag) {
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "操作成功", result);
        } else {
            return WrapMapper.wrap(Wrapper.ERROR_CODE, errorMsg, result);
        }
    }

    private boolean isFlag(Object result) {
        boolean flag;
        if (result instanceof Integer) {
            flag = (Integer) result > 0;
        } else if (result instanceof Boolean) {
            flag = (Boolean) result;
        } else {
            flag = PublicUtil.isNotEmpty(result);
        }
        return flag;
    }

}
