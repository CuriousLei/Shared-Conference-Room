package com.ximingxing.blog.server.utils;

import com.ximingxing.blog.server.pojo.Room;
import com.ximingxing.blog.server.vo.RoomVo;

import java.util.ArrayList;
import java.util.List;

public class RoomUtils {
    public static List<RoomVo> roomVoList(List<Room> l) {
        List<RoomVo> ans = new ArrayList<>();
        for (Room r : l) {
            ans.add(new RoomVo(r));
        }
        return ans;
    }
}
