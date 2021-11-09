package com.tanhua.dubbo.api;

import com.tanhua.mongo.Friends;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/9
 */

public interface FriendApi {
    List<Friends> getAllFriends(Long userId);
}
