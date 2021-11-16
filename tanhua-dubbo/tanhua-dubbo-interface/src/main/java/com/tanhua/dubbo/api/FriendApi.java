package com.tanhua.dubbo.api;

import com.tanhua.mongo.Friend;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/9
 */
public interface FriendApi {
  List<Friend> getAllByUserId(Long userId);

  void save(Friend friend);

  Boolean isFriend(Long userId, Long friendId);
}
