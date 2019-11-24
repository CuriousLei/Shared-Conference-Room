package com.ximingxing.blog.server.dao;

import com.ximingxing.blog.server.pojo.Staff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StaffMapper {

    int insert(Staff record);

    int insertSelective(Staff record);

    int selectLastInsertId();

    List<Staff> selectByConfId(Integer confId);

    Staff selectByConfIdAndUserId(Integer conferenceId, Integer userId);

    int updateByPrimaryKeySelective(Staff staff);
}