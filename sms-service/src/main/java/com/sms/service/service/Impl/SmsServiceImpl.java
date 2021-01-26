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

    public static String SMS_URL = "http://218.205.111.16:8098/sa/sendSms";
    public static String TOKEN = "75D673F7B28C8E7D5BF36EC6533E8BA0B3B30DEF04D861D722EAEA19";

    @Override

    public Map<String, String> sendSmsByStuName(Map map) {
        logger.warn("门禁推送数据是--------------------------> {}", map);
        SmsDto smsDto = new SmsDto();
        smsDto.setSchoolName(map.get("schoolName").toString());
        smsDto.setClassName(map.get("className").toString());
        smsDto.setStudentName(map.get("studentName").toString());
        String type = map.get("type").toString();
        smsDto.setToken(TOKEN);
        smsDto.setAction("smsToPersonByStuName");
        smsDto.setSourceMobile("10657061854");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH点mm分");
        String time = dateTimeFormatter.format(LocalDateTime.now());
        String content = smsDto.getStudentName() + "，您好，现在是" + time + ", 您的小孩已" + type + "，请您关注！五中关怀孩子每一天。";
        smsDto.setContent(content);
        String doPost = HttpUtils.DO_POST(SMS_URL, Encrypt.encode(new Gson().toJson(smsDto)), null, null);
        logger.warn("发送结果 {}", doPost);
        String decode = Encrypt.decode(doPost);
        logger.warn("解密结果--------------> {}", decode);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(smsDto.getStudentName(), decode);
        return hashMap;
    }
}
