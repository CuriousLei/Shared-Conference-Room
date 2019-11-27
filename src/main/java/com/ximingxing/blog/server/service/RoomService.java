package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.vo.RoomVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface RoomService {

    ServerResponse<List<RoomVo>> uploadFile(MultipartFile multipartFile, Integer userId);

    ServerResponse<List<RoomVo>> getRoomsByPageId(Integer pageId);

    ServerResponse<RoomVo> updateRoom(RoomVo roomVo, Integer roomId, Integer curUserRole);

    ServerResponse<RoomVo> deleteRoom(Integer roomId, Integer curUserId);
}
