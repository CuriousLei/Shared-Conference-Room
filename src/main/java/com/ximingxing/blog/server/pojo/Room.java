package com.ximingxing.blog.server.pojo;

import lombok.Data;

@Data
public class Room {
    private Integer roomId;

    private String roomName;

    private String roomDesc;

    private Integer roomNums;

    private Byte roomStatus;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc == null ? null : roomDesc.trim();
    }

    public Integer getRoomNums() {
        return roomNums;
    }

    public void setRoomNums(Integer roomNums) {
        this.roomNums = roomNums;
    }

    public Byte getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Byte roomStatus) {
        this.roomStatus = roomStatus;
    }
}