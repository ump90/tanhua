package com.tanhua.dubbo.api;

import com.tanhua.dubbo.utils.IdWorker;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Friend;
import com.tanhua.mongo.Movement;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@DubboService
@Service
public class MovementApiImpl implements MovementApi {
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private IdWorker idWorker;
  @Autowired private TimeLineApi timeLineApi;
  @Autowired private FriendApi friendApi;

  @Override
  public void publish(Movement movement) {
    movement.setPid(idWorker.getNextId("movement"));
    movement.setCreated(System.currentTimeMillis());
    ObjectId id = ObjectId.get();
    movement.setId(id);
    mongoTemplate.save(movement);
    timeLineApi.saveMovement(id, movement.getUserId());
  }

  @Override
  public List<Movement> list(Long userId, Integer page, Integer pageSize) {
    Criteria criteria = new Criteria();
    criteria.and("userId").is(userId);
    return getMovements(page, pageSize, criteria);
  }

  @Override
  public List<Movement> list(Long userId, Integer page, Integer pageSize, Integer status) {
    Criteria criteria = new Criteria();
    criteria.and("userId").is(userId).and("status").is(status);
    return getMovements(page, pageSize, criteria);
  }

  private List<Movement> getMovements(Integer page, Integer pageSize, Criteria criteria) {
    Sort sort = Sort.by(Sort.Order.asc("created"));
    Query query = new Query();
    query.addCriteria(criteria);
    query.with(sort);
    query.skip((long) (page - 1) * pageSize);
    query.limit(pageSize);
    return mongoTemplate.find(query, Movement.class);
  }

  @Override
  public Long countByUserId(Long userId) {
    Query query = new Query(Criteria.where("userId").is(userId));
    return mongoTemplate.count(query, Movement.class);
  }

  @Override
  public Long countByUserId(Long userId, Integer status) {
    Query query = new Query(Criteria.where("userId").is(userId).and("status").is(status));
    return mongoTemplate.count(query, Movement.class);
  }

  @Override
  public Long countByFriendId(Long friendId) {
    Query query = new Query(Criteria.where("friendId").is(friendId));
    return mongoTemplate.count(query, Movement.class);
  }

  @Override
  public List<Movement> listFriends(Long userId, Integer page, Integer pageSize) {
    List<Friend> friends = friendApi.getAllByUserId(userId);
    List<Long> friendIds = new ArrayList<>(friends.size());
    friends.forEach(
        friend -> {
          friendIds.add(friend.getUserId());
        });
    Query query = new Query(Criteria.where("userId").in(friendIds));
    return mongoTemplate.find(query, Movement.class);
  }

  @Override
  public List<Movement> getByPid(List<Long> pidList) {
    Query query = new Query(Criteria.where("pid").in(pidList));
    return mongoTemplate.find(query, Movement.class);
  }

  @Override
  public List<Movement> getRandom(Integer number) {
    TypedAggregation<Movement> aggregation =
        Aggregation.newAggregation(Movement.class, Aggregation.sample(number));
    AggregationResults<Movement> movements = mongoTemplate.aggregate(aggregation, Movement.class);
    return movements.getMappedResults();
  }

  @Override
  public Movement getById(String id) {
    return mongoTemplate.findById(id, Movement.class);
  }

  @Override
  public void updateWithComment(Comment comment) {
    Integer commentType = comment.getCommentType();
    Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
    if (movement != null) {
      if (commentType == CommentType.COMMENT.getType()) {
        movement.setCommentCount(movement.getCommentCount() + 1);
      } else if (commentType == CommentType.LIKE.getType()) {

      }
    }
  }
}
