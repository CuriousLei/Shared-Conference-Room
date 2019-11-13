package com.ximingxing.blog.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomVo {

    private Integer roomId;

    private String roomName;

    private String roomDesc;

    private Integer roomNums;

    private Byte roomStatus;

}
