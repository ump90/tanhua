package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Movement;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.CommentVo;
import com.tanhua.vo.PageVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/11
 */
@Service
public class CommentService {
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private CommentApi commentApi;
  @DubboReference private MovementApi movementApi;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public PageVo listByMovementId(Integer page, Integer pageSize, String movementId) {
    int count = Math.toIntExact(commentApi.countByMovementId(movementId));
    Integer pages = PageUtil.convertPage(pageSize, count);
    List<Comment> commentList = commentApi.listByMovementId(page, pageSize, movementId);
    List<Long> userIds = CollUtil.getFieldValues(commentList, "userId", Long.class);
    List<UserInfo> userInfoList = userInfoApi.listByIds(userIds);

    Map<Long, UserInfo> userInfoHashMap = CollUtil.fieldValueMap(userInfoList, "id");

    List<CommentVo> commentVos = new ArrayList<>();
    Long userId = UserThreadLocal.getId();
    commentList.forEach(
        comment -> {
          UserInfo userInfo = userInfoHashMap.get(comment.getUserId());
          CommentVo commentVo = CommentVo.init(userInfo, comment);
          String redisKey = Constants.COMMENTS_INTERACT_KEY + comment.getId().toHexString();
          String hashKey = Constants.MOVEMENT_LIKE_HASHKEY + userId;
          if (redisTemplate.opsForHash().hasKey(redisKey, hashKey)) {
            commentVo.setHasLiked(1);
          }
          commentVos.add(commentVo);
        });
    return PageVo.builder()
        .page(page)
        .pagesize(pageSize)
        .pages(pages)
        .counts(count)
        .items(commentVos)
        .build();
  }

  public void postComment(String movementId, String comment) {
    Long userId = UserThreadLocal.getId();
    Movement movement = movementApi.getById(movementId);
    commentApi.save(
        Comment.builder()
            .commentType(CommentType.COMMENT.getType())
            .created(System.currentTimeMillis())
            .publishId(new ObjectId(movementId))
            .likeCount(0)
            .userId(userId)
            .content(comment)
            .publishUserId(movement.getUserId())
            .build());
    movement.setCommentCount(movement.getCommentCount() + 1);
  }

  public Integer commentAction(CommentType commentType, Boolean actionDirection, String id) {

    Boolean isToComment = commentApi.checkIsToComment(id);
    Long userId = UserThreadLocal.getId();
    Boolean isCommented = commentApi.isCommented(id, userId, commentType);
    // 检查是否重复操作
    if (actionDirection) {
      if (isCommented) {
        switch (commentType.getType()) {
          case 1:
            throw new BusinessException(ErrorResult.likeError());
          case 3:
            throw new BusinessException(ErrorResult.loveError());
          default:
            break;
        }
      }
    } else {
      if (!isCommented) {
        switch (commentType.getType()) {
          case 1:
            throw new BusinessException(ErrorResult.disLikeError());
          case 3:
            throw new BusinessException(ErrorResult.disloveError());
          default:
            break;
        }
      }
    }
    // 构造redisKey

    String hashKey = null;
    String redisKey = null;
    Long publishUserId = null;
    if (isToComment) {
      Comment comment = commentApi.getById(id);
      publishUserId = comment.getUserId();
      redisKey = Constants.COMMENTS_INTERACT_KEY + id;
      if (commentType.getType() == 1) {
        hashKey = Constants.MOVEMENT_LIKE_HASHKEY + userId;
      } else if (commentType.getType() == 3) {
        hashKey = Constants.MOVEMENT_LOVE_HASHKEY + userId;
      }

    } else {
      Movement movement = movementApi.getById(id);
      publishUserId = movement.getUserId();
      redisKey = Constants.MOVEMENTS_INTERACT_KEY + id;
      if (commentType.getType() == 1) {
        hashKey = Constants.MOVEMENT_LIKE_HASHKEY + userId;
      } else if (commentType.getType() == 3) {
        hashKey = Constants.MOVEMENT_LOVE_HASHKEY + userId;
      }
    }

    if (hashKey == null) {
      throw new RuntimeException("redis hashKey is null");
    }
    Comment comment =
        Comment.builder()
            .commentType(commentType.getType())
            .publishId(new ObjectId(id))
            .userId(userId)
            .created(System.currentTimeMillis())
            .publishUserId(publishUserId)
            .build();
    if (actionDirection) {
      redisTemplate.opsForHash().put(redisKey, hashKey, 1);
      return commentApi.save(comment);
    } else {
      redisTemplate.opsForHash().delete(redisKey, hashKey);
      return commentApi.delete(comment);
    }
  }
}
