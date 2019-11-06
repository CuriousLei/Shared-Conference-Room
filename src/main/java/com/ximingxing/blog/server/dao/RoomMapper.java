package com.ximingxing.blog.server.dao;

import com.ximingxing.blog.server.pojo.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {
    int deleteByPrimaryKey(Integer roomId);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer roomId);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);
}