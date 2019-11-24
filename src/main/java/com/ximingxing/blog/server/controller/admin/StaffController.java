package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.service.StaffService;
import com.ximingxing.blog.server.vo.StaffVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    /**
     * 获取签到表
     * @param request req
     * @return 签到表
     */
    @GetMapping("/registerList/{confId}")
    public ServerResponse<List<StaffVo>> getRecords(@PathVariable Integer confId, HttpServletRequest request) {
        /*
         * TODO: 获得当前用户id, 这里先写死成超级管理员
         */
        Integer curUserId = 1;

        ServerResponse<List<StaffVo>> ans = staffService.getStaffCheckInTableByConfId(confId);

        if (ResponseCode.SUCCESS.getCode() != ans.getStatus()) {
            log.info("获取签到表信息错误");
        }

        return ans;

    }


}