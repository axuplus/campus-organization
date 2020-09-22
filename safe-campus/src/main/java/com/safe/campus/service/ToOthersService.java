package com.safe.campus.service;

import com.safe.campus.about.utils.wrapper.Wrapper;
import com.safe.campus.model.dto.OthersDto;
import com.safe.campus.model.dto.SelectStudentListDto;

import java.util.List;
import java.util.Map;

public interface ToOthersService {


    Wrapper<Map<Long, String>> getAllMasters();

    Wrapper<Map<Long, String>> getAllClassesByMasterId(Long masterId);

    OthersDto getIdNumberByUserId(String type, Long userId);

    List<SelectStudentListDto> selectStudentList(Map map);

}
