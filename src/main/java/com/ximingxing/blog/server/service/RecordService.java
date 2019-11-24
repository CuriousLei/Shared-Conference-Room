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

    ServerResponse<List<RecordVo>> getApplyResultByUserName(String userName, Integer curUserId);

    ServerResponse<List<RecordVo>> getAllApplyResult(Integer curUserId);

    ServerResponse<List<Record>> uploadBatchApply(MultipartFile file);

    ServerResponse<RecordVo> applyRoom(Record record, MultipartFile file);

    ServerResponse<List<RecordVo>> getRecordsByPageId(Integer pageId);
}
