package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.vo.RoomVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface RoomService {

    ServerResponse<List<Room>> uploadFile(MultipartFile multipartFile, Integer userId);

    ServerResponse<List<Room>> getRoomsByPageId(Integer pageId);

    ServerResponse<Room> updateRoom(RoomVo roomVo, Integer roomId, Integer curUserRole);
}
