package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Record;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface RecordService {

    ServerResponse<List<Record>> uploadFile(MultipartFile file);

}
