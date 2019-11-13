package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.service.RecordService;
import com.ximingxing.blog.server.utils.DozerUtils;
import com.ximingxing.blog.server.vo.RecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/conferBatchApply")
    public String conferBatchApply() {
        return "conferBatchApply";
    }


    /**
     * 通过上传Excel文件的方式批量提交申请
     */
    @PostMapping("/conferBatchApply")
    @ResponseBody
    public ServerResponse<List<RecordVo>> conferBatchApply(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ServerResponse.createByError("上传文件失败");
        }

        ServerResponse<List<Record>> isUpload = recordService.uploadFile(file);

        int status = isUpload.getStatus();
        if (ResponseCode.SUCCESS.getCode() != status) {
            // 上传失败
            return ServerResponse.createByError(isUpload.getMsg());
        }

        // 上传成功

        // 生成VO
        ServerResponse<List<RecordVo>> ret = ServerResponse.createBySuccess(isUpload.getMsg(),
                DozerUtils.mapList(isUpload.getData(), RecordVo.class));

        return ret;
    }


}