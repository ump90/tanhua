package com.tanhua.dubbo.api;

import com.tanhua.enums.CommentTarget;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Movement;
import com.tanhua.mongo.Video;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/11
 */
@DubboService
@Slf4j
@Service
public class CommentApiImpl implements CommentApi {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public List<Comment> listByPublishId(Integer page, Integer pageSize, String publishId) {
    Query query =
        new Query(
            Criteria.where("publishId")
                .is(new ObjectId(publishId))
                .and("commentType")
                .is(CommentType.COMMENT));
    query.limit(page).skip((long) pageSize * (page - 1)).with(Sort.by(Sort.Order.desc("created")));
    return mongoTemplate.find(query, Comment.class);
  }

  @Override
  public Long countByPublishId(String publishId) {
    Query query =
        new Query(
            Criteria.where("publishId")
                .is(new ObjectId(publishId))
                .and("commentType")
                .is(CommentType.COMMENT));
    return mongoTemplate.count(query, Comment.class);
  }

  @Override
  public Integer save(Comment comment, CommentTarget commentTarget) {
    mongoTemplate.save(comment);
    boolean isToComment = checkIsToComment(commentTarget);
    return modifyMovementAndComment(comment, 1, !isToComment, commentTarget);
  }

  @Override
  public Integer delete(Comment comment, CommentTarget commentTarget) {
    Query query = new Query();
    query.addCriteria(Criteria.where("publishId").is(comment.getPublishId()));
    query.addCriteria(Criteria.where("userId").is(comment.getUserId()));
    query.addCriteria(Criteria.where("commentType").is(comment.getCommentType()));
    mongoTemplate.remove(query, Comment.class);
    boolean isToComment = checkIsToComment(commentTarget);
    return modifyMovementAndComment(comment, -1, !isToComment, commentTarget);
  }

  @Override
  public Boolean isCommented(String movementId, Long userId, CommentType commentType) {
    Query query = new Query();
    query.addCriteria(Criteria.where("publishId").is(new ObjectId(movementId)));
    query.addCriteria(Criteria.where("userId").is(userId));
    query.addCriteria(Criteria.where("commentType").is(commentType.getType()));
    return mongoTemplate.exists(query, Comment.class);
  }

  @Override
  public Boolean checkIsToComment(CommentTarget commentTarget) {
    return commentTarget.equals(CommentTarget.Comment);
  }

  @Override
  public Comment getById(String id) {
    Query query = new Query(Criteria.where("id").is(id));
    return mongoTemplate.findOne(query, Comment.class);
  }

  private Integer modifyMovementAndComment(
      Comment comment, Integer interval, boolean isMovement, CommentTarget commentTarget) {
    Integer type = comment.getCommentType();
    Update update = new Update();
    FindAndModifyOptions options = new FindAndModifyOptions();
    options.returnNew(true);
    if (type == CommentType.LIKE.getType()) {
      update.inc("likeCount", interval);
    } else if (type == CommentType.COMMENT.getType()) {
      update.inc("commentCount", interval);
    } else if (type == CommentType.LOVE.getType()) {
      update.inc("loveCount", interval);
    }
    if (isMovement) {
      Query query = new Query(Criteria.where("id").is(comment.getPublishId()));

      if (commentTarget == CommentTarget.Movement) {
        Movement movement = mongoTemplate.findAndModify(query, update, options, Movement.class);
        if (movement != null) {
          if (type == CommentType.LIKE.getType()) {
            return movement.getLikeCount();
          } else if (type == CommentType.COMMENT.getType()) {
            return movement.getCommentCount();
          } else if (type == CommentType.LOVE.getType()) {
            return movement.getLoveCount();
          }
        } else {
          return -1;
        }
      } else {
        Video video = mongoTemplate.findAndModify(query, update, options, Video.class);
        if (video == null) {
          return -1;
        } else {
          if (type == CommentType.LIKE.getType()) {
            return video.getLikeCount();
          } else if (type == CommentType.COMMENT.getType()) {
            return video.getCommentCount();
          } else if (type == CommentType.LOVE.getType()) {
            return video.getLoveCount();
          }
        }
      }

    } else {
      Query query = new Query(Criteria.where("id").is(comment.getPublishId()));
      Comment returnedComment = mongoTemplate.findAndModify(query, update, options, Comment.class);
      if (returnedComment != null) {
        return returnedComment.getLikeCount();
      }
      return -1;
    }

    return -1;
  }

  @Override
  public PageVo listByUserId(Integer page, Integer pageSize, Long userId, CommentType commentType) {
    Query query =
        new Query(
                Criteria.where("publishUserId")
                    .is(userId)
                    .and("commentType")
                    .is(commentType.getType()))
            .with(Sort.by(Sort.Order.desc("created")))
            .skip((long) (page - 1) * pageSize)
            .limit(pageSize);

    List<Comment> commentList = mongoTemplate.find(query, Comment.class);
    Query queryCount =
        new Query(
            Criteria.where("publishUserId")
                .is(userId)
                .and("commentType")
                .is(commentType.getType()));
    int count = Math.toIntExact(mongoTemplate.count(queryCount, Comment.class));
    Integer pages = PageUtil.convertPage(pageSize, count);
    return PageVo.builder()
        .page(page)
        .pages(pages)
        .counts(count)
        .pagesize(pageSize)
        .items(commentList)
        .build();
  }
}
