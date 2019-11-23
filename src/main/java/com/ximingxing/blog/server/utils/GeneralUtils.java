package com.ximingxing.blog.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class GeneralUtils {

    /**
     * 获得当前时间
     *
     * @param s 时间格式，例如"yyyy-MM-dd_HH-mm-ss"
     * @return 当前时间字符串
     */
    public static String getCurrentData(String s) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(s);
        return dateTime.format(formatter);
    }

    public static Date transStringToDate(String dateString, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
