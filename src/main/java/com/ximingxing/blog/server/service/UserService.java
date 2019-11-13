package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.vo.UserVo;

/**
 * Description:
 * Created By xxm
 */
public interface UserService {

    ServerResponse<User> login(String userName, String password);

    ServerResponse<User> addUser(User user);

    ServerResponse<String> logout(String userName);
}
