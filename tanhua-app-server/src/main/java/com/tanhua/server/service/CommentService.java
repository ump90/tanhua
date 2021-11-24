package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.enums.CommentTarget;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Movement;
import com.tanhua.mongo.Video;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.CommentVo;
import com.tanhua.vo.MessageUserVo;
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
  @DubboReference private VideoApi videoApi;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public PageVo listById(Integer page, Integer pageSize, String movementId) {
    int count = Math.toIntExact(commentApi.countByPublishId(movementId));
    Integer pages = PageUtil.convertPage(pageSize, count);
    List<Comment> commentList = commentApi.listByPublishId(page, pageSize, movementId);
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
          String likeHashKey = Constants.MOVEMENT_LIKE_HASHKEY + userId;
          String loveHashKey = Constants.MOVEMENT_LOVE_HASHKEY + userId;
          if (redisTemplate.opsForHash().hasKey(redisKey, likeHashKey)) {
            commentVo.setHasLiked(1);
          }
          if (redisTemplate.opsForHash().hasKey(redisKey, loveHashKey)) {
            commentVo.setHashLoved(1);
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

  public void postComment(String id, String comment, CommentTarget commentTarget) {
    Long userId = UserThreadLocal.getId();
    ObjectId pushlishId = null;
    Long publishUserId = null;
    if (commentTarget == CommentTarget.Movement) {
      Movement movement = movementApi.getById(id);
      pushlishId = movement.getId();
      publishUserId = movement.getUserId();
    }

    if (commentTarget == CommentTarget.Video) {

      Video video = videoApi.getById(id);
      pushlishId = video.getId();
      publishUserId = video.getUserId();
    }

    Comment comment1 =
        Comment.builder()
            .commentType(CommentType.COMMENT.getType())
            .created(System.currentTimeMillis())
            .publishId(pushlishId)
            .likeCount(0)
            .userId(userId)
            .content(comment)
            .publishUserId(publishUserId)
            .build();
    commentApi.save(comment1, commentTarget);
  }

  public Integer commentAction(
      CommentType commentType, Boolean actionDirection, String id, CommentTarget commentTarget) {

    Boolean isToComment = commentApi.checkIsToComment(commentTarget);
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
      return commentApi.save(comment, commentTarget);
    } else {
      redisTemplate.opsForHash().delete(redisKey, hashKey);
      return commentApi.delete(comment, commentTarget);
    }
  }

  public PageVo getLovesOrLikes(Integer page, Integer pageSize, CommentType commentType) {
    Long id = UserThreadLocal.getId();
    PageVo pageVo = commentApi.listByUserId(page, pageSize, id, commentType);
    List<?> commentList = pageVo.getItems();
    List<Long> userIds = CollUtil.getFieldValues(commentList, "userId", Long.class);
    List<UserInfo> userInfos = userInfoApi.listByIds(userIds);
    Map<Long, UserInfo> userInfoMap = CollUtil.fieldValueMap(userInfos, "id");
    List<MessageUserVo> messageUserVoList = new ArrayList<>(commentList.size());
    commentList.forEach(
        comment -> {
          Comment comment1 = (Comment) comment;
          UserInfo userInfo = userInfoMap.get(comment1.getUserId());
          MessageUserVo messageUserVo = MessageUserVo.init(comment1, userInfo);
          messageUserVoList.add(messageUserVo);
        });

    pageVo.setItems(messageUserVoList);
    return pageVo;
  }
}
