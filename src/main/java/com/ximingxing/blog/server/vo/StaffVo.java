package com.ximingxing.blog.server.vo;

import com.ximingxing.blog.server.pojo.Staff;
import com.ximingxing.blog.server.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffVo {
    private Integer staffId;

    private Integer conferenceId;

    private String staffName;

    private String staffDesc;

    private String staffStatus;

    private String staffCheckintime;

    public StaffVo(Staff staff) {
        setStaffId(staff.getStaffId());
        setConferenceId((staff.getConferenceId()));
        setStaffName(staff.getStaffName());
        setStaffDesc(staff.getStaffDesc());

        setStaffStatus(staff.getStaffStatus() == 0 ? "unattended" : "attended");
        if (1 == staff.getStaffStatus()) {
            setStaffCheckintime(GeneralUtils.transDateToString(staff.getStaffCheckintime(), "yyyy-MM-dd HH:mm:ss"));
        }
    }
}