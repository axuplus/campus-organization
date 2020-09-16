package com.safe.campus.service;

import com.safe.campus.about.utils.wrapper.Wrapper;

import java.util.Map;

public interface ToOthersService {


    Wrapper<Map<Long, String>> getAllMasters();


    Wrapper<Map<Long, String>> getAllClassesByMasterId(Long masterId);

}
