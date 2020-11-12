package com.smoke.service.controller;

import com.smoke.service.service.SmokeDataService;
import com.smoke.service.service.SmokeDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smoke")
public class SmokeController {

    @Autowired
    private SmokeDeviceService deviceService;

    @Autowired
    private SmokeDataService dataService;



}
