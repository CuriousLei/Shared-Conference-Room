package com.ximingxing.blog.server.service.impl;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.dao.UserMapper;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.UserService;
import com.ximingxing.blog.server.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Created By xxm
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<UserVo> login(String userName, String password) {
        User user = userMapper.selectByUsernameAndPassword(userName, password);

        UserVo userVo = new UserVo(user);
        System.out.println(user);

        if (user != null) {
            return ServerResponse.createBySuccess(userVo);
        }
        return ServerResponse.createByError("没有用户或用户密码错误");
    }

    @Override
    public ServerResponse<User> addUser(User user) {
        return null;
    }

    @Override
    public ServerResponse<String> logout(String userName) {
        return null;
    }
}