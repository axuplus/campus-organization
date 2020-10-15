package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.model.domain.LanrunOpenLog;

public interface SynchronizeDataService extends IService<LanrunOpenLog> {

    void SynchronizeDataFromFuji();
}
