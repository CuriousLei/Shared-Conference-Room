package com.ximingxing.blog.server.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.service.RecordService;
import com.ximingxing.blog.server.utils.DozerUtils;
import com.ximingxing.blog.server.utils.GeneralUtils;
import com.ximingxing.blog.server.utils.RecordUtils;
import com.ximingxing.blog.server.vo.RecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService recordService;


    /**
     * 申请会议室表单上传
     *
     * @return 分配结果列表
     */
    @PostMapping(value = "/roomInfoApply", consumes = {"multipart/form-data"})
    public ServerResponse<RecordVo> applyRoom(HttpServletRequest request) {
        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        MultipartFile file = null;
        if (request instanceof StandardMultipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> fileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
            file = fileMap.get("file").get(0);
        }
        String jsonString = request.getParameter("jsonData");

        RecordVo recordVo = null;
        try {
            recordVo = new ObjectMapper().readValue(jsonString.getBytes(), RecordVo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByError("JSON解析失败");
        }

        recordVo.setUserId(curUserId);
        Record record = RecordUtils.transToRecordFromRecordVoWithSpan(recordVo);

        return recordService.applyRoom(record, file);
    }

    /**
     * 通过所有查询会议室分配结果
     *
     * @return 分配结果列表
     */
    @GetMapping("/applyResult")
    public ServerResponse<List<RecordVo>> getApplyResultByUserName(HttpServletRequest request) {
        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        return recordService.getAllApplyResult(curUserId);
    }


    /**
     * 通过用户名查询会议室分配结果
     *
     * @param userName 用户名
     * @return 分配结果列表
     */
    @GetMapping("/applyResult/{username}")
    public ServerResponse<List<RecordVo>> getApplyResultByUserName(
            @PathVariable("username") String userName,
            HttpServletRequest request
    ) {
        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        return recordService.getApplyResultByUserName(userName, curUserId);
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

        ServerResponse<List<Record>> isUpload = recordService.uploadBatchApply(file);

        int status = isUpload.getStatus();
        if (ResponseCode.SUCCESS.getCode() != status) {
            // 上传失败
            return ServerResponse.createByError(isUpload.getMsg());
        }

        // 上传成功

        return ServerResponse.createBySuccess(isUpload.getMsg(),
                DozerUtils.mapList(isUpload.getData(), RecordVo.class));
    }


}