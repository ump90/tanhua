package com.tanhua.dubbo.api;

import com.tanhua.mongo.UserLike;

/**
 * @author UMP90
 * @date 2021/11/15
 */
public interface UserLikeApi {
  void save(UserLike userLike);

  Boolean isLike(Long userId, Long likeUserId);
}
