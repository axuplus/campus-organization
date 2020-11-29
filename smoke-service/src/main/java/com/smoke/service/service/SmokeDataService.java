package com.smoke.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smoke.service.model.entity.SmokeData;
import com.smoke.service.utils.wrapper.Wrapper;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Joma
 * @since 2020-11-12
 */
public interface SmokeDataService extends IService<SmokeData> {

    Wrapper saveResponse(String response);

    String test(String msg, String nonce, String signature);

}
