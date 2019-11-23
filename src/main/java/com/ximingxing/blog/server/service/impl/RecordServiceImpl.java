package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RecordMapper;
import com.ximingxing.blog.server.dao.RoomMapper;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.RecordService;
import com.ximingxing.blog.server.utils.RecordUtils;
import com.ximingxing.blog.server.utils.UserUtils;
import com.ximingxing.blog.server.vo.RecordVo;
import com.ximingxing.blog.server.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

        List<RecordVo> records = recordMapper.selectAll();

        if (null == records) {
            log.info("查询失败");
            return ServerResponse.createByError("查询失败");
        }
        log.info("查询成功");

        for (RecordVo recordVo : records) {
            Byte roomStatus = recordVo.getRoomStatus();
            if (1 == roomStatus || 2 == roomStatus) {
                // 需要添加Room信息
                Integer roomId = recordVo.getRoomId();
                Room room = roomMapper.selectByPrimaryKey(roomId);
                if (null == room) {
                    log.info("roomId=" + roomId + " 会议室信息丢失");
                    return ServerResponse.createByError("部分会议室信息丢失");
                }
                recordVo.setRoomVo(new RoomVo(room));
            }
        }

        return ServerResponse.createBySuccess("查询成功", records);
    }


    @Override
    public ServerResponse<List<Record>> uploadBatchApply(MultipartFile file) {
        File dest = RecordUtils.uploadFile(file);
        if (null == dest) {
            return ServerResponse.createByError("上传失败");
        }

        List<Record> records = RecordUtils.analyseFileToList(dest);
        if (records == null) {
            return ServerResponse.createByError("解析为空");
        }
        log.info("list: " + records.toString());

        int iCount = InsertRecordListIntoSQL(records);

        return ServerResponse.createBySuccess("批量添加了" + iCount + "个申请", records);
    }


    private int InsertRecordIntoSQL(Record record) {

        if (null == record) {
            return 0;
        }
        return recordMapper.insertSelective(record);
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
}

