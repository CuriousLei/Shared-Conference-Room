package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.Constant;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RoomMapper;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.RoomService;
import com.ximingxing.blog.server.utils.RoomUtils;
import com.ximingxing.blog.server.utils.UserUtils;
import com.ximingxing.blog.server.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<List<RoomVo>> uploadFile(MultipartFile file, Integer userId) {
        String fileName;
        fileName = file.getOriginalFilename();

        String filePath;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            filePath = "c:\\RoomsTempFiles\\";
        } else {
            filePath = "/Users/RoomsTempFiles/";
        }

        ExistFilePath(filePath);

        File dest = new File(filePath + GetCurrentData("yyyy-MM-dd_HH-mm-ss") + fileName);

        try {
            file.transferTo(dest);
            log.info("上传文件成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
            return ServerResponse.createByError("上传失败");
        }

        List<Room> list;
        try {
            list = excelToRoomList(dest.getPath());
            log.info("解析文件成功");
        } catch (ParseException e) {
            log.info("解析文件失败");
            e.printStackTrace();
            return ServerResponse.createByError("解析文件失败");
        }

        log.info("list: " + list.toString());

        for (Room room : list) {
            room.setUserId(userId);
        }
        int iCount = InsertRoomListIntoSQL(list);

        if (0 == iCount) {
            log.info("插入数据库失败");
            return ServerResponse.createByError("插入数据库失败");
        }

        return ServerResponse.createBySuccess("批量添加了" + iCount + "个会议室", RoomUtils.roomVoList(list));
    }

    /**
     * 获得制定页码的会议室信息列表
     * @param pageId 给定页码
     * @return 成功：会议室列表；失败：失败原因。
     */
    @Override
    public ServerResponse<List<RoomVo>> getRoomsByPageId(Integer pageId) {
        List<Room> list = roomMapper.selectAll();

        int maxSize = list.size();
        int startIndex = pageId * Constant.PAGESIZE;
        if (startIndex > maxSize || startIndex < 0) {
            return ServerResponse.createByError("页面错误");
        }

        int endIndex = startIndex + Constant.PAGESIZE;
        if (endIndex > maxSize) {
            endIndex = maxSize;
        }

        return ServerResponse.createBySuccess("获取成功", RoomUtils.roomVoList(list.subList(startIndex, endIndex)));
    }


    /**
     * 修改会议室信息
     * @param roomVo 更新的数据源
     * @param roomId 待修改会议室id
     * @param curUserId 修改者role
     * @return 成功：更新后的Room；失败：失败原因
     */
    @Override
    public ServerResponse<RoomVo> updateRoom(RoomVo roomVo, Integer roomId, Integer curUserId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        User curUser = userMapper.selectByPrimaryKey(curUserId);
        User uploadUser = userMapper.selectByPrimaryKey(room.getUserId());
        // 权限校验
        if (!UserUtils.roleTest(uploadUser, curUser)) {
            log.info("尝试修改会议室，权限不够");
            return ServerResponse.createByError("权限不够");
        }
        log.info("尝试修改会议室，权限足够");

        // 更新数据
        room.setRoomName(roomVo.getRoomName());
        room.setRoomDesc(roomVo.getRoomDesc());
        room.setRoomNums(roomVo.getRoomNums());

        // 更新数据库
        int ret = roomMapper.updateByPrimaryKey(room);

        if (0 == ret) {
            log.info("写入数据库失败");
            ServerResponse.createByError("写入数据库失败");
        }
        log.info("写入数据库成功");

        return ServerResponse.createBySuccess("更新成功", new RoomVo(room));
    }

    /**
     * 删除会议室
     * @param roomId 会议室id
     * @param curUserId 删除者id
     * @return 成功：已删除的Room；失败：失败原因
     */
    @Override
    public ServerResponse<RoomVo> deleteRoom(Integer roomId, Integer curUserId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        User curUser = userMapper.selectByPrimaryKey(curUserId);
        User uploadUser = userMapper.selectByPrimaryKey(room.getUserId());
        // 权限校验
        if (!UserUtils.roleTest(uploadUser, curUser)) {
            log.info("尝试删除会议室，权限不够");
            return ServerResponse.createByError("权限不够");
        }
        log.info("尝试删除会议室，权限足够");

        room.setRoomStatus((byte)1);
        int ret = roomMapper.updateByPrimaryKeySelective(room);

        if (0 == ret) {
            log.info("写入数据库失败");
            ServerResponse.createByError("写入数据库失败");
        }
        log.info("写入数据库成功");

        return ServerResponse.createBySuccess("删除成功", new RoomVo(room));
    }

    /**
     * 插入数据库
     *
     * @param list 会议室列表
     */
    private int InsertRoomListIntoSQL(List<Room> list) {
        int iCount = 0;
        for (Room r : list) {
            iCount += roomMapper.insertSelective(r);
        }
        return iCount;
    }

    /**
     * 确保路径存在
     *
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
