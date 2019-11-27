package com.ximingxing.blog.server.utils;

import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.vo.RoomVo;
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
public class RoomUtils {

    public static List<RoomVo> roomVoList(List<Room> l) {
        List<RoomVo> ans = new ArrayList<>();
        for (Room r : l) {
            ans.add(new RoomVo(r));
        }
        return ans;
    }

    public static List<Room> analyseFileToList(File dest) {
        List<Room> list = null;
        try {
            list = excelToRoomList(dest.getPath());
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
     * 本地方法，分析Excel得出会议室信息列表
     *
     * @param filePath 文件路径
     * @return List<Room>
     */
    private static List<Room> excelToRoomList(String filePath) throws ParseException {

        List<Room> list = new ArrayList<>();

        try {

            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));

            XSSFSheet sheet = wb.getSheetAt(0);
            if (null != sheet) {
                Room room = null;
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); ++rowNum) {
                    XSSFRow row = sheet.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    room = new Room();

                    // 获取单元格生肉值
                    room.setRoomName(row.getCell(0).getRawValue());
                    room.setRoomDesc(row.getCell(1).getRawValue());
                    // 强制转化数字
                    room.setRoomNums((int) row.getCell(2).getNumericCellValue());
                    room.setRoomStatus(new Byte("0"));

                    log.info("room: " + room.toString());
                    list.add(room);
                }
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
