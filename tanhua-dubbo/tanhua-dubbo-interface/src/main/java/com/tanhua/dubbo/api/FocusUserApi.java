package com.tanhua.dubbo.api;

/**
 * @author UMP90
 * @date 2021/11/19
 */
public interface FocusUserApi {
  void focus(Long userId, Long followedUserId, boolean flag);
}
