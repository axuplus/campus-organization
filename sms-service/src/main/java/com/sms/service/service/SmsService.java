package com.sms.service.service;

import com.sms.service.utils.SmsDto;

import java.util.Map;

public interface SmsService {

    Map<String, String> sendSmsByStuName( Map map);
}
