package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Created By xxm
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        User user = userMapper.selectByUsernameAndPassword(userName, password);

        System.out.println(user);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByError("没有用户或用户密码错误");
    }

    @Override
    public ServerResponse<User> addUser(User user) {
        return null;
    }

    @Override
    public ServerResponse<String> logout(String userName) {

        log.info(userName + " 已注销");

        return ServerResponse.createBySuccess("注销成功", userName + " 已注销");
    }
}