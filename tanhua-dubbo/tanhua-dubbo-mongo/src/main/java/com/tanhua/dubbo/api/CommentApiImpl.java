package com.tanhua.dubbo.api;

import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Movement;
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
  public List<Comment> listByMovementId(Integer page, Integer pageSize, String movementId) {
    Query query =
        new Query(
            Criteria.where("publishId")
                .is(new ObjectId(movementId))
                .and("commentType")
                .is(CommentType.COMMENT));
    query.limit(page).skip((long) pageSize * (page - 1)).with(Sort.by(Sort.Order.desc("created")));
    return mongoTemplate.find(query, Comment.class);
  }

  @Override
  public Long countByMovementId(String movementId) {
    Query query =
        new Query(
            Criteria.where("publishId")
                .is(new ObjectId(movementId))
                .and("commentType")
                .is(CommentType.COMMENT));
    return mongoTemplate.count(query, Comment.class);
  }

  @Override
  public Integer save(Comment comment) {
    mongoTemplate.save(comment);
    boolean isToComment = checkIsToComment(comment.getPublishId().toHexString());
    return modifyMovementAndComment(comment, 1, !isToComment);
  }

  @Override
  public Integer delete(Comment comment) {
    Query query = new Query();
    query.addCriteria(Criteria.where("publishId").is(comment.getPublishId()));
    query.addCriteria(Criteria.where("userId").is(comment.getUserId()));
    query.addCriteria(Criteria.where("commentType").is(comment.getCommentType()));
    mongoTemplate.remove(query, Comment.class);
    boolean isToComment = checkIsToComment(comment.getPublishId().toHexString());
    return modifyMovementAndComment(comment, -1, !isToComment);
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
  public Boolean checkIsToComment(String id) {
    Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
    return mongoTemplate.exists(query, Comment.class);
  }

  @Override
  public Comment getById(String id) {
    Query query = new Query(Criteria.where("id").is(id));
    return mongoTemplate.findOne(query, Comment.class);
  }

  private Integer modifyMovementAndComment(Comment comment, Integer interval, boolean isMovement) {
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
      Query query = new Query(Criteria.where("id").is(comment.getPublishId()));
      Comment returnedComment = mongoTemplate.findAndModify(query, update, options, Comment.class);
      if (returnedComment != null) {
        return returnedComment.getLikeCount();
      }
      return -1;
    }

    return -1;
  }
}
