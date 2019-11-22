package com.ximingxing.blog.server.utils;

import com.ximingxing.blog.server.pojo.User;

public class UserUtils {

    /**
     * 判断给定User是否高于目标User
     * @param aimUser 目标id
     * @param testUser 给定id
     * @return 是否满足
     */
    public static Boolean roleTest(User aimUser, User testUser) {

        Integer aimId = aimUser.getUserId();
        Integer testId = testUser.getUserId();

        Byte aimRole = aimUser.getUserRole();
        Byte testRole = testUser.getUserRole();

        if (0 == testRole) {
            return Boolean.TRUE;
        }

        if (2 == testRole) {
            return aimRole != 0;
        }

        // 剩下testId
        if (5 == testRole || 10 == testRole || 15 == testRole) {
            return testId.equals(aimId);
        }

        return Boolean.FALSE;
    }

}
