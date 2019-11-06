package com.ximingxing.blog.server.pojo;

public class Staff {
    private Integer staffId;

    private Integer conferenceId;

    private String staffName;

    private String staffDesc;

    private Byte staffStatus;

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
        this.staffName = staffName == null ? null : staffName.trim();
    }

    public String getStaffDesc() {
        return staffDesc;
    }

    public void setStaffDesc(String staffDesc) {
        this.staffDesc = staffDesc == null ? null : staffDesc.trim();
    }

    public Byte getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(Byte staffStatus) {
        this.staffStatus = staffStatus;
    }
}