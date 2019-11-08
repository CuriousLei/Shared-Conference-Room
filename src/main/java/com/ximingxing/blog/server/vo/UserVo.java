package com.ximingxing.blog.server.vo;

import com.ximingxing.blog.server.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Integer userId;

    private String userName;

    private String userDesc;

    /**
     * Vo中，角色需要从数字对应到名称
     * 0 超级管理员
     * 2 会议室管理员
     * 5.一级用户/10.二级用户/15.三级用户
      */
    private String userRole;

    private String userAlias;

    private String userPasswd;

    public UserVo(User user) {
        userId = user.getUserId();
        userName = user.getUserName();
        userDesc = user.getUserDesc();
        userAlias = user.getUserAlias();
        Byte userRoleCode = user.getUserRole();
        switch (userRoleCode) {
            case 0:
                userAlias = "超级管理员";
                break;
            case 2:
                userAlias = "会议室管理员";
                break;
            case 5:
            case 10:
            case 15:
                userAlias = "普通用户";
                break;
            default:
                userAlias = "未知人员类别";
        }
    }
}