package com.safe.campus.service.Impl;

import com.safe.campus.service.MqMessageService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqMessageServiceImpl implements MqMessageService {


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Object sendSynchronizeMessages(String routineKey, String mqSysDto) {
        amqpTemplate.convertAndSend("people.exchange", routineKey, mqSysDto);
        return true;
    }

}
