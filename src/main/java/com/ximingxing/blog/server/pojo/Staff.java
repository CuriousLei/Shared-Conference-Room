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
}