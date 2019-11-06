package com.ximingxing.blog.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;

    private String userAlias;

    private String userName;

    private String userPasswd;

    private String userDesc;

    private Byte userRole;
}