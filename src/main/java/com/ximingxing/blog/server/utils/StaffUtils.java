package com.ximingxing.blog.server.utils;

import com.ximingxing.blog.server.pojo.Staff;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StaffUtils {

    public static List<Staff> analyseFileWithSingleTableToList(File dest) {
        List<Staff> list = null;
        try {
            list = excelToStaffList(dest.getPath());
            log.info("解析文件成功");
        } catch (ParseException e) {
            log.info("解析文件失败");
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * Created By nzz
     * 本地方法，分析Excel得出与会人员id和姓名
     *
     * @param filePath 文件路径
     * @return List<Staff>
     */
    private static List<Staff> excelToStaffList(String filePath) throws ParseException {

        List<Staff> list = new ArrayList<>();

        try {

            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));

            XSSFSheet sheet = wb.getSheetAt(0);
            if (null != sheet) {
                Staff staff = null;
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); ++rowNum) {
                    XSSFRow row = sheet.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    staff = new Staff();

                    //userId	userName
                    //1    张三
                    staff.setStaffId((int) row.getCell(0).getNumericCellValue());
                    staff.setStaffName(row.getCell(1).getStringCellValue());
                    staff.setStaffStatus((byte) 0);

                    log.info("staff: " + staff.toString());
                    list.add(staff);
                }
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
