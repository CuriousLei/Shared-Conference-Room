package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     */
    @PostMapping(value = "/login")
    public ServerResponse<User> login(@RequestBody User user, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getUserName())) {
            ServerResponse.createByError("用户名不存在");
        }
        if (StringUtils.isEmpty(user.getUserPasswd())) {
            ServerResponse.createByError("密码为空");
        }
        ServerResponse<User> isLogin = userService.login(user.getUserName(), user.getUserPasswd());
        User curUser = isLogin.getData();
        log.debug(new StringBuilder().append(curUser.getUserId()).toString());
        request.getSession().setAttribute(
                new StringBuilder().append(curUser.getUserId()).toString(), curUser
        );

        log.info(user.getUserName() + " 已登陆");

        return isLogin;
    }

    /**
     * 用户注销
     */
    @PostMapping(value = "/logout")
    public ServerResponse<String> logout(@RequestBody User user, HttpServletRequest request) {

        ServerResponse<String> isLogout = userService.logout(user.getUserName());

        log.info("注销前 session: " + request.getSession().getAttributeNames().hashCode());

        request.getSession().removeAttribute(new StringBuilder().append(user.getUserId()).toString());

        log.info(user.getUserName() + " 已注销");
        log.info("注销后 session: " + request.getSession().getAttributeNames().hashCode());

        return isLogout;
    }
}
