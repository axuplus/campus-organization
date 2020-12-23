package com.sms.service.controller;

import com.sms.service.service.SmsService;
import com.sms.service.utils.SmsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {


    @Autowired
    private SmsService smsService;

    @PostMapping("/sendSmsByStuName")
    public Map sendSmsByStuName(@RequestBody Map map){
        return smsService.sendSmsByStuName(map);
    }
}
