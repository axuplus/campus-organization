package com.safe.campus.service;


import com.safe.campus.model.dto.MqSysDto;

public interface MqMessageService {

    Object sendSynchronizeMessages(String routineKey, String mqSysDto);
}
