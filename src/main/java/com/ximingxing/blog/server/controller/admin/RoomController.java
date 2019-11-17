package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.service.RoomService;
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

    @GetMapping("/roomInfoBatchAdd")
    public String roomInfoBatchAdd() {
        return "roomInfoBatchAdd";
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

        ServerResponse<List<Room>> isUpload = roomService.uploadFile(file, userId);

        int status = isUpload.getStatus();
        if (ResponseCode.SUCCESS.getCode() != status) {
            // 上传失败
            return ServerResponse.createByError(isUpload.getMsg());
        }

        // 上传成功


        return isUpload;
    }


}
