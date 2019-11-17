package com.ximingxing.blog.server.controller.admin;

import com.ximingxing.blog.server.common.ResponseCode;
import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.User;
import com.ximingxing.blog.server.service.UserService;
import com.ximingxing.blog.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
    public ServerResponse<UserVo> login(@RequestBody User user, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getUserName())) {
            return ServerResponse.createByError("用户名不存在");
        }
        if (StringUtils.isEmpty(user.getUserPasswd())) {
            return ServerResponse.createByError("密码为空");
        }
        ServerResponse<User> isLogin = userService.login(user.getUserName(), user.getUserPasswd());

        int status = isLogin.getStatus();
        if (ResponseCode.SUCCESS.getCode() != status) {
            // 登陆失败
            return ServerResponse.createByError(isLogin.getMsg());
        }

        // 登陆成功
        User curUser = isLogin.getData();

        log.debug(String.valueOf(curUser.getUserId()));

        request.getSession().setAttribute("userId", curUser.getUserId());

        log.info(curUser.getUserName() + " 已登陆");

        // 生成VO
        ServerResponse<UserVo> ret = ServerResponse.createBySuccess(isLogin.getMsg(), new UserVo(curUser));

        return ret;
    }

    /**
     * 用户注销
     */
    @PostMapping(value = "/logout")
    public ServerResponse<String> logout(@RequestBody User user, HttpServletRequest request) {

        ServerResponse<String> isLogout = userService.logout(user.getUserName());

        log.info("注销前 session: " + request.getSession().getAttributeNames().hashCode());

        request.getSession().removeAttribute(String.valueOf(user.getUserId()));

        log.info("注销后 session: " + request.getSession().getAttributeNames().hashCode());

        return isLogout;
    }
}
