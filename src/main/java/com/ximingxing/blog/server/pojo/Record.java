package com.ximingxing.blog.server.pojo;

import java.util.Date;

public class Record {
    private Integer conferenceId;

    private Integer roomId;

    private String conferenceName;

    private String conferenceDesc;

    private Integer userId;

    private Date conferenceStart;

    private Date conferenceEnd;

    private Byte roomStatus;

    private Integer conferenceNums;

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName == null ? null : conferenceName.trim();
    }

    public String getConferenceDesc() {
        return conferenceDesc;
    }

    public void setConferenceDesc(String conferenceDesc) {
        this.conferenceDesc = conferenceDesc == null ? null : conferenceDesc.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getConferenceStart() {
        return conferenceStart;
    }

    public void setConferenceStart(Date conferenceStart) {
        this.conferenceStart = conferenceStart;
    }

    public Date getConferenceEnd() {
        return conferenceEnd;
    }

    public void setConferenceEnd(Date conferenceEnd) {
        this.conferenceEnd = conferenceEnd;
    }

    public Byte getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Byte roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Integer getConferenceNums() {
        return conferenceNums;
    }

    public void setConferenceNums(Integer conferenceNums) {
        this.conferenceNums = conferenceNums;
    }
}