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
import java.util.HashMap;
import java.util.List;

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
    HashMap<Long, UserInfo> userInfoHashMap = new HashMap<>();
    userInfoList.forEach(
        userInfo -> {
          userInfoHashMap.put(userInfo.getId(), userInfo);
        });
    List<CommentVo> commentVos = new ArrayList<>();
    commentList.forEach(
        comment -> {
          UserInfo userInfo = userInfoHashMap.get(comment.getUserId());
          commentVos.add(CommentVo.init(userInfo, comment));
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

  public Integer like(String movementId) {
    Movement movement = movementApi.getById(movementId);
    Long userId = UserThreadLocal.getId();
    Boolean isLiked = commentApi.isLiked(movementId, userId);
    if (isLiked) {
      throw new BusinessException(ErrorResult.likeError());
    }
    Comment comment =
        Comment.builder()
            .publishUserId(movement.getUserId())
            .userId(userId)
            .created(System.currentTimeMillis())
            .commentType(CommentType.LIKE.getType())
            .publishId(new ObjectId(movementId))
            .build();

    Integer count = commentApi.save(comment);
    String redisMovementId = Constants.MOVEMENT_LIKE_HASHKEY + String.valueOf(movementId);
    redisTemplate.opsForHash().put(redisMovementId, String.valueOf(comment.getUserId()), 1);
    return count;
  }

  public Integer unlike(String movementId) {
    Movement movement = movementApi.getById(movementId);
    Long userId = UserThreadLocal.getId();
    Boolean isLiked = commentApi.isLiked(movementId, userId);
    if (!isLiked) {
      throw new BusinessException(ErrorResult.disLikeError());
    }
    Comment comment =
        Comment.builder()
            .commentType(CommentType.LIKE.getType())
            .publishId(new ObjectId(movementId))
            .userId(userId)
            .build();
    String redisMovementId = Constants.MOVEMENT_LIKE_HASHKEY + String.valueOf(movementId);
    redisTemplate.opsForHash().delete(redisMovementId, String.valueOf(comment.getUserId()));
    return commentApi.delete(comment);
  }

  public Integer love(String movementId) {
    Movement movement = movementApi.getById(movementId);
    Long userId = UserThreadLocal.getId();
    Boolean isLoved = commentApi.isLiked(movementId, userId);
    if (isLoved) {
      throw new BusinessException(ErrorResult.likeError());
    }
    Comment comment =
        Comment.builder()
            .publishUserId(movement.getUserId())
            .userId(userId)
            .created(System.currentTimeMillis())
            .commentType(CommentType.LOVE.getType())
            .publishId(new ObjectId(movementId))
            .build();

    Integer count = commentApi.save(comment);
    String redisMovementId = Constants.MOVEMENT_LOVE_HASHKEY + String.valueOf(movementId);
    redisTemplate.opsForHash().put(redisMovementId, String.valueOf(comment.getUserId()), 1);
    return count;
  }

  public Integer unlove(String movementId) {
    Movement movement = movementApi.getById(movementId);
    Long userId = UserThreadLocal.getId();
    Boolean isLoved = commentApi.isLiked(movementId, userId);
    if (!isLoved) {
      throw new BusinessException(ErrorResult.disloveError());
    }
    Comment comment =
        Comment.builder()
            .commentType(CommentType.LOVE.getType())
            .publishId(new ObjectId(movementId))
            .userId(userId)
            .build();
    String redisMovementId = Constants.MOVEMENT_LOVE_HASHKEY + String.valueOf(movementId);
    redisTemplate.opsForHash().delete(redisMovementId, String.valueOf(comment.getUserId()));
    return commentApi.delete(comment);
  }
}
