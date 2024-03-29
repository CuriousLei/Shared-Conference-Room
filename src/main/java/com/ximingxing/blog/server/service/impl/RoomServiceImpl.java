package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.Constant;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RoomMapper;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.RoomService;
import com.ximingxing.blog.server.utils.GeneralUtils;
import com.ximingxing.blog.server.utils.RoomUtils;
import com.ximingxing.blog.server.utils.UserUtils;
import com.ximingxing.blog.server.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        File dest = GeneralUtils.uploadFile(file);
        if (null == dest) {
            return ServerResponse.createByError("上传失败");
        }

        List<Room> list = RoomUtils.analyseFileToList(dest);
        if (list == null) {
            return ServerResponse.createByError("解析为空");
        }
        log.info("list: " + list);

        for (Room room : list) {
            room.setUserId(userId);
        }
        int iCount = insertRoomListIntoSQL(list);

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
    private int insertRoomListIntoSQL(List<Room> list) {
        int iCount = 0;
        for (Room r : list) {
            iCount += roomMapper.insertSelective(r);
        }
        return iCount;
    }

}
