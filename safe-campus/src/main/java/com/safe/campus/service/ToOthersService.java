package com.safe.campus.service;

import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;

import java.util.Map;

public interface ToOthersService {


    Wrapper<Map<Long, String>> getAllMasters();

    Wrapper<Map<Long, String>> getAllClassesByMasterId(Long masterId);

    OthersDto getIdNumberByUserId(String type, Long userId);

}
