package com.tanhua.dubbo.api;

import com.tanhua.mongo.Comment;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/11
 */
public interface CommentApi {
  List<Comment> listByMovementId(Integer page, Integer pageSize, String movementId);

  Long countByMovementId(String movementId);

  Integer save(Comment comment);

  Boolean isLiked(String movementId, Long userId);

  Integer delete(Comment comment);

  Boolean isLoved(String movementId, Long userId);
}
