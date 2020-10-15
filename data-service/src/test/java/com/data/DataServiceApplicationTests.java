package com.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@Slf4j
class DataServiceApplicationTests {

    @Test
    void contextLoads() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String pre = df.format(calendar.getTime());
        log.info("now========> {}", now);
        log.info("pre========> {}", pre);
        System.out.println("DateFormat.getDateInstance().format(new Date()) = " + DateFormat.getDateInstance().format(new Date()));
    }

}
