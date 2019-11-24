package com.ximingxing.blog.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private Integer conferenceId;

    private Integer roomId;

    private String conferenceName;

    private String conferenceDesc;

    private Integer userId;

    private Date conferenceStart;

    private Date conferenceEnd;

    private Byte roomStatus;    // 会议室的申请进程/0.申请未分配/1.已分配未开始/2.会议已经结束/3.分配失败

    private Integer conferenceNums;
}