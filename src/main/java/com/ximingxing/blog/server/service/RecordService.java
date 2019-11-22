package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.vo.RecordVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface RecordService {

    ServerResponse<List<Record>> uploadFile(MultipartFile file);

    ServerResponse<List<RecordVo>> getApplyResultByUserName(String userName, Integer curUserId);
}
