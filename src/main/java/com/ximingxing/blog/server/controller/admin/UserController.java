package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     */
    @PostMapping(value = "/login")
    public ServerResponse<User> getArticles(@RequestBody User user, HttpSession session) {
        if (StringUtils.isEmpty(user.getUserName())) {
            ServerResponse.createByError("用户名不存在");
        }
        if (StringUtils.isEmpty(user.getUserPasswd())) {
            ServerResponse.createByError("密码为空");
        }
        ServerResponse<User> isLogin = userService.login(user.getUserName(), user.getUserPasswd());
        User curUser = isLogin.getData();
        session.setAttribute(new StringBuilder().append(curUser.getUserId()).toString(), curUser);
        return isLogin;
    }

}
