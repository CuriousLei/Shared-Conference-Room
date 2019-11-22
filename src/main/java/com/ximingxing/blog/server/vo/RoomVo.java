package com.ximingxing.blog.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ximingxing.blog.server.pojo.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomVo {

    @JsonProperty("room_id")
    private Integer roomId;

    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("room_desc")
    private String roomDesc;

    @JsonProperty("room_nums")
    private Integer roomNums;

    @JsonProperty("room_status")
    private String roomStatus;

    public RoomVo(Room room) {
        setRoomId(room.getRoomId());
        setRoomName(room.getRoomName());
        setRoomDesc(room.getRoomDesc());
        setRoomNums(room.getRoomNums());
        switch (room.getRoomStatus()) {
            case 0: setRoomStatus("该会议室授权可用");
            break;
            case 1: setRoomStatus("该会议室暂时不可使用");
        }
    }
}
