package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RecordMapper;
import com.ximingxing.blog.server.dao.RoomMapper;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.RecordService;
import com.ximingxing.blog.server.utils.UserUtils;
import com.ximingxing.blog.server.utils.XSSFDateUtil;
import com.ximingxing.blog.server.vo.RecordVo;
import com.ximingxing.blog.server.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Description:
 * Created By nzz
 */
@Slf4j
@Service
@Component
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public ServerResponse<List<Record>> uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

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

        List<Record> list = null;
        try {
            list = excelToRecordList(dest.getPath());
            log.info("解析文件成功");
        } catch (ParseException e) {
            log.info("解析文件失败");
            e.printStackTrace();
            return ServerResponse.createByError("解析文件失败");
        }

        log.info("list: " + list.toString());

        int iCount = InsertRecordListIntoSQL(list);

        ServerResponse<List<Record>> ret = ServerResponse.createBySuccess("批量添加了" + iCount + "个申请", list);
        return ret;
    }

    /**
     * 通过用户名获取申请列表
     *
     * @param userName  用户名
     * @param curUserId 当前用户id
     * @return 成功：userName的所有申请记录；失败：失败原因
     */
    @Override
    public ServerResponse<List<RecordVo>> getApplyResultByUserName(String userName, Integer curUserId) {

        User aimUser = userMapper.selectByUserName(userName);
        User curUser = userMapper.selectByPrimaryKey(curUserId);
        // 权限校验
        if (!UserUtils.roleTest(aimUser, curUser)) {
            log.info("尝试查询申请记录，权限不够");
            return ServerResponse.createByError("权限不够");
        }
        log.info("尝试查询申请记录，权限足够");

        List<Record> records = recordMapper.selectByUserId(aimUser.getUserId());

        if (null == records) {
            log.info("查询失败");
            return ServerResponse.createByError("查询失败");
        }
        log.info("查询成功");

        List<RecordVo> ans = new ArrayList<RecordVo>();
        for (Record record : records) {
            RecordVo recordVo = new RecordVo(record);
            Byte roomStatus = record.getRoomStatus();
            if (1 == roomStatus || 2 == roomStatus) {
                // 需要添加Room信息
                Integer roomId = record.getRoomId();
                Room room = roomMapper.selectByPrimaryKey(roomId);
                if (null == room) {
                    log.info("roomId=" + roomId + " 会议室信息丢失");
                    return ServerResponse.createByError("部分会议室信息丢失");
                }
                recordVo.setRoomVo(new RoomVo(room));
            }
            ans.add(recordVo);
        }

        return ServerResponse.createBySuccess("查询成功", ans);
    }

    @Override
    public ServerResponse<List<RecordVo>> getAllApplyResult(Integer curUserId) {

        User curUser = userMapper.selectByPrimaryKey(curUserId);
        // 权限校验
        if (0 != curUser.getUserRole() && 2 != curUser.getUserRole()) {
            log.info("尝试查询申请记录，权限不够");
            return ServerResponse.createByError("权限不够");
        }
        log.info("尝试查询申请记录，权限足够");

        List<Record> records = recordMapper.selectAll();

        if (null == records) {
            log.info("查询失败");
            return ServerResponse.createByError("查询失败");
        }
        log.info("查询成功");

        List<RecordVo> ans = new ArrayList<>();
        for (Record record : records) {
            RecordVo recordVo = new RecordVo(record);
            Byte roomStatus = record.getRoomStatus();
            if (1 == roomStatus || 2 == roomStatus) {
                // 需要添加Room信息
                Integer roomId = record.getRoomId();
                Room room = roomMapper.selectByPrimaryKey(roomId);
                if (null == room) {
                    log.info("roomId=" + roomId + " 会议室信息丢失");
                    return ServerResponse.createByError("部分会议室信息丢失");
                }
                recordVo.setRoomVo(new RoomVo(room));
            }
            ans.add(recordVo);
        }

        return ServerResponse.createBySuccess("查询成功", ans);
    }

    /**
     * 插入数据库
     *
     * @param list 申请列表
     */
    private int InsertRecordListIntoSQL(List<Record> list) {
        int iCount = 0;
        for (Record r : list) {
            iCount += recordMapper.insertSelective(r);
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
     * 本地方法，分析Excel得出申请信息列表
     *
     * @param filePath 文件路径
     * @return List<Record>
     */
    private List<Record> excelToRecordList(String filePath) throws ParseException {

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
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                    String startString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(3).getNumericCellValue()));
                    String endString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(4).getNumericCellValue()));

                    Date start = transStringToDate(dataString + "-" + startString, "yyyy/MM/dd-hh:mm:ss");
                    Date end = transStringToDate(dataString + "-" + endString, "yyyy/MM/dd-hh:mm:ss");

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

    private Date transStringToDate(String dateString, String formatString) {
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

