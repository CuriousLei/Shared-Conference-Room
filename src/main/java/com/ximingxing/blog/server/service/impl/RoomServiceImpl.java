package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RoomMapper;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created By nzz
 */
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public ServerResponse<List<Room>> uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = "C:\\RoomsTempFiles\\";

        ExistFilePath(filePath);

        File dest = new File(filePath + GetCurrentData("yyyy-MM-dd_HH-mm-ss") + fileName);

        try {
            file.transferTo(dest);
            log.info("上传文件成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
            return ServerResponse.createByError("上传失败");
        }

        List<Room> list = null;
        try {
            list = excelToRoomList(dest.getPath());
            log.info("解析文件成功");
        } catch (ParseException e) {
            log.info("解析文件失败");
            e.printStackTrace();
            return ServerResponse.createByError("解析文件失败");
        }

        log.info("list: " + list.toString());

        int iCount = InsertRoomListIntoSQL(list);

        ServerResponse<List<Room>> ret = ServerResponse.createBySuccess("批量添加了" + iCount + "个会议室", list);
        return ret;
    }

    /**
     * 插入数据库
     * @param list 会议室列表
     */
    private int InsertRoomListIntoSQL(List<Room> list) {
        int iCount = 0;
        for (Room r : list){
            iCount += roomMapper.insertSelective(r);
        }
        return iCount;
    }

    /**
     * 确保路径存在
     * @param filePath 文件夹路径
     */
    private void ExistFilePath(String filePath) {
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
     * 本地方法，分析Excel得出会议室信息列表
     *
     * @param filePath 文件路径
     * @return List<Room>
     */
    private List<Room> excelToRoomList(String filePath) throws ParseException {

        List<Room> list = new ArrayList<Room>();

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


    /**
     * 获得当前时间
     *
     * @param s 时间格式
     * @return 当前时间字符串
     */
    private String GetCurrentData(String s) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(s);
        return dateTime.format(formatter);
    }
}
