package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface RoomService {

    ServerResponse<List<Room>> uploadFile(MultipartFile multipartFile, Integer userId);

}
