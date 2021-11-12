package com.tanhua.utils;

/**
 * @author UMP90
 * @date 2021/11/10
 */
public class PageUtil {

  public static Integer convertPage(Integer pageSize, Integer count) {
    if (count == 0) {
      return 0;
    } else {
      int pages = count / pageSize;
      if (count % pageSize != 0) {
        ++pages;
      }
      return pages;
    }
  }
}
