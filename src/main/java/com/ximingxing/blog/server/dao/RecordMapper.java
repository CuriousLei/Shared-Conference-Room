package com.ximingxing.blog.server.dao;

import com.ximingxing.blog.server.pojo.Record;

public interface RecordMapper {
    int deleteByPrimaryKey(Integer conferenceId);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer conferenceId);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);
}