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

    private Byte roomStatus;
}