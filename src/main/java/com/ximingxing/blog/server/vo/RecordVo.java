package com.ximingxing.blog.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ximingxing.blog.server.pojo.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo {

    @JsonProperty("conf_id")
    private Integer conferenceId;

    @JsonProperty("room_id")
    private Integer roomId;

    @JsonProperty("conf_name")
    private String conferenceName;

    @JsonProperty("conf_desc")
    private String conferenceDesc;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("conf_start")
    @JsonFormat(pattern = "yyyy/MM/dd-HH:mm:ss", timezone = "GMT+8")
    private Date conferenceStart;

    @JsonProperty("conf_end")
    @JsonFormat(pattern = "yyyy/MM/dd-HH:mm:ss", timezone = "GMT+8")
    private Date conferenceEnd;

    @JsonProperty("room_status")
    private Byte roomStatus;    // 会议室的申请进程/0.申请未分配/1.已分配未开始/2.会议已经结束/3.分配失败

    @JsonProperty("conf_nums")
    private Integer conferenceNums;

    @JsonProperty("room")
    private RoomVo roomVo;

    public RecordVo(Record record) {
        setConferenceId(record.getConferenceId());
        setConferenceName(record.getConferenceName());
        setConferenceDesc(record.getConferenceDesc());
        setUserId(record.getUserId());
        setConferenceStart(record.getConferenceStart());
        setConferenceEnd(record.getConferenceEnd());
        setRoomStatus(record.getRoomStatus());
        setConferenceNums(record.getConferenceNums());
    }
}