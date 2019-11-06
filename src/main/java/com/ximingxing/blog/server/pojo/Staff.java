package com.ximingxing.blog.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    private Integer staffId;

    private Integer conferenceId;

    private String staffName;

    private String staffDesc;

    private String staffConfs;

    private Byte staffStatus;
}