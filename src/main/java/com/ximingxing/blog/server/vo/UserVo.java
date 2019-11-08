package com.ximingxing.blog.server.vo;

import com.ximingxing.blog.server.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Integer user_id;

    private String user_name;

    private String user_desc;

    /**
     * Vo中，角色需要从数字对应到名称
     * 0 超级管理员
     * 2 会议室管理员
     * 5.一级用户/10.二级用户/15.三级用户
      */
    private String user_role;

    private String user_alias;

    private String user_password;

    public UserVo(User user) {
        user_id = user.getUserId();
        user_name = user.getUserName();
        user_desc = user.getUserDesc();
        user_alias = user.getUserAlias();
        setUser_role(getUser_roleByUserRole(user.getUserRole()));
    }

    private String getUser_roleByUserRole(Byte userRoleCode) {

        switch (userRoleCode) {
            case 0:
                return "super admin";
            case 2:
                return "admin";
            case 5:
            case 10:
            case 15:
                return "user";
            default:
                return "unknown";
        }
    }


}