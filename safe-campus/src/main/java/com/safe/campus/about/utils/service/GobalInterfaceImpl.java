package com.safe.campus.about.utils.service;

import com.safe.campus.about.utils.IdWorker;
import com.safe.campus.about.utils.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class GobalInterfaceImpl implements GobalInterface {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Long generateId() {
        Long id = null;
        try {
            id = new IdWorker().nextId();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return id;
    }

    @Override
    public Long currentTime() {
        return DateUtil.currentTime(new Date());
    }
}
