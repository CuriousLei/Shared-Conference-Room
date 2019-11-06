package com.ximingxing.blog.server.dao;

import com.ximingxing.blog.server.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    User selectByUsernameAndPassword(@Param("username") String username, @Param("passwd") String passwd);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}