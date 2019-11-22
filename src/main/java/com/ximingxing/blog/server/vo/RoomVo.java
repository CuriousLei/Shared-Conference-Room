package com.ximingxing.blog.server.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomVo {

    private Integer roomId;

    @JsonAlias("room_name")
    private String roomName;

    @JsonAlias("room_desc")
    private String roomDesc;

    @JsonAlias("room_nums")
    private Integer roomNums;

    private Byte roomStatus;

}
