package com.tanhua.server.utils;

import com.tanhua.pojo.User;

/**
 * @author UMP90
 * @date 2021/11/3
 */

public class UserThreadLocal {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        threadLocal.set(user);
    }

    public static User getUser() {
        return threadLocal.get();
    }

    public static Long getId() {
        return threadLocal.get().getId();
    }

    public static String getMobile() {
        return threadLocal.get().getMobile();
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
