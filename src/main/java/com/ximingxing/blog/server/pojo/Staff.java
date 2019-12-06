package com.ximingxing.blog.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    private Integer staffId;

    private Integer conferenceId;

    private String staffName;

    private String staffDesc;

    private Byte staffStatus;

    private Date staffCheckintime;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffDesc() {
        return staffDesc;
    }

    public void setStaffDesc(String staffDesc) {
        this.staffDesc = staffDesc;
    }

    public Byte getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(Byte staffStatus) {
        this.staffStatus = staffStatus;
    }

    public Date getStaffCheckintime() {
        return staffCheckintime;
    }

    public void setStaffCheckintime(Date staffCheckintime) {
        this.staffCheckintime = staffCheckintime;
    }
}