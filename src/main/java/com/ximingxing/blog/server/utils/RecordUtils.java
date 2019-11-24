package com.ximingxing.blog.server.utils;

import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.vo.RecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class RecordUtils {

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

        RecordUtils.isExistFilePath(filePath);

        File dest = new File(filePath + GeneralUtils.getCurrentData("yyyy-MM-dd_HH-mm-ss") + fileName);

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error(e.toString(), e);
            return null;
        }

        return dest;
    }


    public static List<Record> analyseFileToList(File dest) {
        List<Record> list = null;
        try {
            list = excelToRecordList(dest.getPath());
            log.info("解析文件成功");
        } catch (ParseException e) {
            log.info("解析文件失败");
            e.printStackTrace();
            return null;
        }
        return list;
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

    /**
     * Created By nzz
     * 本地方法，分析Excel得出申请信息列表
     *
     * @param filePath 文件路径
     * @return List<Record>
     */
    private static List<Record> excelToRecordList(String filePath) throws ParseException {

        List<Record> list = new ArrayList<Record>();

        try {

            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));

            XSSFSheet sheet = wb.getSheetAt(0);
            if (null != sheet) {
                Record record = null;
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); ++rowNum) {
                    XSSFRow row = sheet.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    record = new Record();

                    //会议名称	会议描述	会议日期	开始时间	结束时间	人数	用户id
                    //这里是名称	这里是描述   2019/11/31	1:02:03	1:05:09	20	123
                    record.setConferenceName(row.getCell(0).getStringCellValue());
                    record.setConferenceDesc(row.getCell(1).getStringCellValue());


                    // 转换Date类型单元格
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    String dataString = simpleDateFormat.format(XSSFDateUtil.getJavaDate(row.getCell(2).getNumericCellValue()));

                    // 转换Time类型单元格
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String startString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(3).getNumericCellValue()));
                    String endString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(4).getNumericCellValue()));

                    Date start = GeneralUtils.transStringToDate(dataString + "-" + startString, "yyyy/MM/dd-HH:mm:ss");
                    Date end = GeneralUtils.transStringToDate(dataString + "-" + endString, "yyyy/MM/dd-HH:mm:ss");

                    record.setConferenceStart(start);
                    record.setConferenceEnd(end);

                    record.setConferenceNums((int) row.getCell(5).getNumericCellValue());
                    record.setUserId((int) row.getCell(6).getNumericCellValue());

                    // 会议室的申请进程/0.申请未分配/1.已分配未开始/2.会议已经结束/3.分配失败
                    record.setRoomStatus(new Byte("0"));

                    log.info("record: " + record.toString());
                    list.add(record);
                }
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Record transToRecordFromRecordVoWithSpan(RecordVo recordVo) {
        Record record = transToRecordFromRecordVo(recordVo);

        String[] split = recordVo.getConferenceSpan().split("-");
        String startString = split[0];
        String endString = split[1];
        Date start = GeneralUtils.transStringToDate(recordVo.getConferenceDate() + "-" + startString, "yyyy-MM-dd-HH:mm");
        Date end = GeneralUtils.transStringToDate(recordVo.getConferenceDate() + "-" + endString, "yyyy-MM-dd-HH:mm");
        record.setConferenceStart(start);
        record.setConferenceEnd(end);

        return record;
    }

    public static Record transToRecordFromRecordVo(RecordVo recordVo) {
        Record record = new Record();

        record.setConferenceId(recordVo.getConferenceId());
        record.setConferenceName(recordVo.getConferenceName());
        record.setConferenceDesc(recordVo.getConferenceDesc());
        record.setUserId(recordVo.getUserId());
        record.setConferenceStart(recordVo.getConferenceStart());
        record.setConferenceEnd(recordVo.getConferenceEnd());
        record.setRoomStatus(recordVo.getRoomStatus() == null ? 0 : recordVo.getRoomStatus());
        record.setConferenceNums(recordVo.getConferenceNums());

        return record;
    }

    public static List<RecordVo> recordVoList(List<Record> l) {
        List<RecordVo> ans = new ArrayList<>();
        for (Record r : l) {
            ans.add(new RecordVo(r));
        }
        return ans;
    }

    // 通过start和end计算date和span
    public static RecordVo calcSpan(RecordVo r) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String tempStart = sdf.format(r.getConferenceStart());
        String tempEnd = sdf.format(r.getConferenceEnd());
        String dateString = tempStart.substring(0, tempStart.indexOf('_'));
        String startString = tempStart.substring(tempStart.indexOf('_') + 1);
        String endString = tempEnd.substring(tempEnd.indexOf('_') + 1);

        r.setConferenceDate(dateString);
        r.setConferenceSpan(startString + '-' + endString);
        return r;
    }
}
