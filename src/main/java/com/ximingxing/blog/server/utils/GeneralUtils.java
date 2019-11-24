package com.ximingxing.blog.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class GeneralUtils {

    /**
     * 获得当前时间
     *
     * @param s 时间格式，例如"yyyy-MM-dd_HH-mm-ss"
     * @return 当前时间字符串
     */
    public static String getCurrentDate(String s) {
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

    public static String transDateToString(Date date, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        return sdf.format(date);
    }

    /**
     * 上传文件到默认路径
     *
     * @param file 前端传入文件
     * @return 成功：本地文件引用；失败：null
     */
    public static File uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        String filePath;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            filePath = "c:\\RoomsTempFiles\\";
        } else {
            filePath = System.getProperty("user.dir") + "\\RoomsTempFiles\\";
        }

        GeneralUtils.isExistFilePath(filePath);

        File dest = new File(filePath + GeneralUtils.getCurrentDate("yyyy-MM-dd_HH-mm-ss") + fileName);

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error(e.toString(), e);
            return null;
        }

        return dest;
    }

    /**
     * 确保路径存在
     *
     * @param filePath 文件夹路径
     */
    private static void isExistFilePath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                log.info("上传目录存在");
            } else {
                log.info("同名文件存在, 不能创建");
            }
        } else {
            log.info("目录不存在，创建目录");
            file.mkdir();
        }
    }
}
