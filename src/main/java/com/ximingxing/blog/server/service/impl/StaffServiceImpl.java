package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.StaffMapper;
import com.ximingxing.blog.server.pojo.Staff;
import com.ximingxing.blog.server.service.StaffService;
import com.ximingxing.blog.server.vo.StaffVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Component
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public ServerResponse<List<StaffVo>> getStaffCheckInTableByConfId(Integer confId) {

        List<Staff> list = staffMapper.selectByConfId(confId);

        if (null == list) {
            log.info("查询失败");
            return ServerResponse.createByError("查询失败");
        }
        log.info("查询成功");

        List<StaffVo> ans = new ArrayList<>();
        for (Staff staff : list) {
            ans.add(new StaffVo(staff));
        }

        return ServerResponse.createBySuccess("查询成功", ans);
    }

}
