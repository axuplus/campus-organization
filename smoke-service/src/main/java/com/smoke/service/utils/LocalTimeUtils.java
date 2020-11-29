package com.smoke.service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 2020/22/17
 */
public class LocalTimeUtils {

    // YY-MM-DD HH-MM-SS
    public static String LocalDateTimeToString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    // YY-MM-DD
    public static String LocalDateTimeToShortString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(now);
    }
    // YY-MM-DD HH-MM-SS
    public static LocalDateTime StringToLocalDateTime(String localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(localDateTime, formatter);
    }


}
