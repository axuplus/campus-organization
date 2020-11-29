package com.smoke.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalUtils {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    public static Long generateId() {
        Long id = null;
        try {
            id = new IdWorker().nextId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

}
