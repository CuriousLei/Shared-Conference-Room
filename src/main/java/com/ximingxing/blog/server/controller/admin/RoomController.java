package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.service.RoomService;
import com.ximingxing.blog.server.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 查询会议室列表
     * @param pageId 页码，每页15个
     * @return 成功：会议室列表；失败：错误信息
     */
    @GetMapping("/roomInfo/page/{pageId}")
    public ServerResponse<List<Room>> getRooms(@PathVariable Integer pageId) {
        ServerResponse<List<Room>> ans = roomService.getRoomsByPageId(pageId);

        if (ResponseCode.ERROR.getCode() != ans.getStatus()) {
            log.info("获取会议室信息错误");
        }

        return ans;
    }

    /**
     * 通过上传Excel文件的方式新增会议室
     */
    @PostMapping("/roomInfoBatchAdd")
    @ResponseBody
    public ServerResponse<List<Room>> roomInfoBatchAdd(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ServerResponse.createByError("上传文件失败");
        }

        Integer userId = (Integer)request.getSession().getAttribute("userId");

        if (null == userId) {
            return ServerResponse.createByError("登陆信息错误");
        }

        ServerResponse<List<Room>> isUpload = roomService.uploadFile(file, userId);

        int status = isUpload.getStatus();
        if (ResponseCode.SUCCESS.getCode() != status) {
            // 上传失败
            return ServerResponse.createByError(isUpload.getMsg());
        }

        // 上传成功


        return isUpload;
    }

    @PutMapping("/roomInfo/{roomId}")
    public ServerResponse<Room> updateRoom(@RequestBody RoomVo roomVo, @PathVariable Integer roomId, HttpServletRequest request) {

        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        return roomService.updateRoom(roomVo, roomId, curUserId);
    }

    /**
     * 通过id删除会议室，仅标记会议室不可用
     * @param roomId 会议室id
     * @param request req
     * @return 该会议室信息
     */
    @DeleteMapping("/roomInfo/{roomId}")
    public ServerResponse<Room> deleteRoom(@PathVariable Integer roomId, HttpServletRequest request) {

        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        return roomService.deleteRoom(roomId, curUserId);
    }

}
