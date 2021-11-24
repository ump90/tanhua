package com.tanhua.admin.utils;

import com.tanhua.pojo.Admin;

/**
 * @author UMP90
 * @date 2021/11/3
 */
public class AdminThreadLocal {
  private static ThreadLocal<Admin> threadLocal = new ThreadLocal<>();

  public static void setAdmin(Admin admin) {
    threadLocal.set(admin);
  }

  public static Long getAdminId() {
    return threadLocal.get().getId();
  }

  public static String getAdminName() {
    return threadLocal.get().getName();
  }

  public static Admin getAdmin() {
    return threadLocal.get();
  }

  public static void remove() {
    threadLocal.remove();
  }
}
