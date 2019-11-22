package com.ximingxing.blog.server.dao;

import com.ximingxing.blog.server.pojo.Record;
import com.ximingxing.blog.server.vo.RecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {
    int deleteByPrimaryKey(Integer conferenceId);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer conferenceId);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    List<Record> selectByUserId(Integer userId);

    List<RecordVo> selectAll();
}