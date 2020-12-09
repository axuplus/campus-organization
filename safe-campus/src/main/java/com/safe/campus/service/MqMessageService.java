package com.safe.campus.service;


public interface MqMessageService {

    Object sendSynchronizeMessages(String routineKey, Object object);
}
