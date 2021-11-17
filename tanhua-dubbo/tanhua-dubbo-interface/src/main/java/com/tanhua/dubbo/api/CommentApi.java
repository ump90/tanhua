package com.tanhua.dubbo.api;

import com.tanhua.enums.CommentType;
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

  Integer delete(Comment comment);

  Boolean isCommented(String movementId, Long userId, CommentType commentType);

  Boolean checkIsToComment(String id);

  Comment getById(String id);
}
