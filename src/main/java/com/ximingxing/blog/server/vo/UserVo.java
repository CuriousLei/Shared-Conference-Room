package com.ximingxing.blog.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ximingxing.blog.server.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_desc")
    private String userDesc;

    @JsonProperty("user_role")
    private String userRole;

    @JsonProperty("user_alias")
    private String userAlias;

    @JsonProperty("user_password")
    private String userPasswd;

    /**
     * Vo中，角色需要从数字对应到名称
     * 0 超级管理员
     * 2 会议室管理员
     * 5.一级用户/10.二级用户/15.三级用户
      */

    public UserVo(User user) {
        setUserId(user.getUserId());
        setUserName(user.getUserName());
        setUserDesc(user.getUserDesc());
        setUserAlias(user.getUserAlias());
        setUserRole(transRoleFromByteToString(user.getUserRole()));
    }

    private String transRoleFromByteToString(Byte userRoleCode) {

        switch (userRoleCode) {
            case 0:
                return "superAdmin";
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