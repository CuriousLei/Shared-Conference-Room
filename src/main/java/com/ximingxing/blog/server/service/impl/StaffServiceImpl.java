package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.RecordMapper;
import com.ximingxing.blog.server.dao.StaffMapper;
import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.pojo.Staff;
import com.ximingxing.blog.server.service.StaffService;
import com.ximingxing.blog.server.utils.GeneralUtils;
import com.ximingxing.blog.server.vo.StaffVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Component
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private RecordMapper recordMapper;

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

    @Override
    public ServerResponse<Staff> checkIn(Integer roomId, Integer userId) {

        // 在Record中查找roomId是否处于已分配且未开始状态
        List<Record> records = recordMapper.selectByRoomIdAndRoomStatus(roomId, 1);

        if (null == records) {
            return ServerResponse.createByError("当前会议室未使用");
        }

        Integer conferenceId = records.get(0).getConferenceId();

        Staff staff = staffMapper.selectByConfIdAndUserId(conferenceId, userId);
        if (null == staff) {
            return ServerResponse.createByError("当前会议没有此人");
        }

        Date now = GeneralUtils.transStringToDate(GeneralUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        staff.setStaffCheckintime(now);
        staff.setStaffStatus((byte) 1);
        int iCount = staffMapper.updateByPrimaryKeySelective(staff);
        if (0 == iCount) {
            return ServerResponse.createByError("签到失败");
        }

        return ServerResponse.createBySuccess("签到成功", staff);
    }

}
