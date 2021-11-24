package com.tanhua.dubbo.api;

import com.tanhua.enums.CommentTarget;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.vo.PageVo;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/11
 */
public interface CommentApi {
  List<Comment> listByPublishId(Integer page, Integer pageSize, String publishId);

  Long countByPublishId(String publishId);

  Integer save(Comment comment, CommentTarget commentTarget);

  Integer delete(Comment comment, CommentTarget commentTarget);

  Boolean isCommented(String movementId, Long userId, CommentType commentType);

  Boolean checkIsToComment(CommentTarget commentTarget);

  Comment getById(String id);

  PageVo listByUserId(Integer page, Integer pageSize, Long userId, CommentType commentType);
}
