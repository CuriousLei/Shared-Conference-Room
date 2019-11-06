package com.ximingxing.blog.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;

    private String userName;

    private String userDesc;

    private Byte userRole;

    private String userAlias;

    private String userPasswd;
}