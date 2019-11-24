package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.vo.StaffVo;

import java.util.List;

/**
 * Description:
 * Created By nzz
 */
public interface StaffService {

    ServerResponse<List<StaffVo>> getStaffCheckInTableByConfId(Integer confId);
}
