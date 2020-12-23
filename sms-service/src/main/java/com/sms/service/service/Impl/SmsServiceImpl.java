package com.sms.service.service.Impl;

import com.google.gson.Gson;
import com.sms.service.service.SmsService;
import com.sms.service.utils.Encrypt;
import com.sms.service.utils.HttpUtils;
import com.sms.service.utils.SmsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String SMS_URL = "http://IP:PORT/sa/smsToPersonByStuName";
    public static String TOKEN = "";

    @Override

    public Map sendSmsByStuName(Map map) {
        logger.warn("门禁推送数据是-------------------------->", map);
        SmsDto smsDto = new SmsDto();
        smsDto.setSchoolName(map.get("schoolName").toString());
        smsDto.setClassName(map.get("className").toString());
        smsDto.setStudentName(map.get("studentName").toString());
        String type = map.get("type").toString();
        smsDto.setToken(TOKEN);
        smsDto.setAction("smsToPersonByStuName");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH点:mm分");
        String time = dateTimeFormatter.format(LocalDateTime.now());
        String content = smsDto.getStudentName() + "，您好，现在是" + time + ",您的小孩已" + type + "，请您关注！五中关怀孩子每一天。";
        smsDto.setContent(content);
        String doPost = HttpUtils.DO_POST(SMS_URL, Encrypt.encode(new Gson().toJson(smsDto)), null, null);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(smsDto.getStudentName(), doPost);
        return hashMap;
    }
}
