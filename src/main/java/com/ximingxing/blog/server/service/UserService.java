package com.ximingxing.blog.server.service;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.User;

/**
 * Description:
 * Created By xxm
 */
public interface UserService {

    ServerResponse<User> login(String userName, String password);
}
