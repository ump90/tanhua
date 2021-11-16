package com.tanhua.dubbo.api;

import com.tanhua.mongo.Friend;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/9
 */
@Service
@DubboService
public class FriendApiImpl implements FriendApi {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public List<Friend> getAllByUserId(Long userId) {
    Query query = new Query(Criteria.where("friendId").is(userId));
    return mongoTemplate.find(query, Friend.class);
  }

  @Override
  public void save(Friend friend) {

    mongoTemplate.save(friend);
  }

  @Override
  public Boolean isFriend(Long userId, Long friendId) {
    Query query = new Query(Criteria.where("userId").is(userId).and("friendId").is(friendId));
    return mongoTemplate.exists(query, Friend.class);
  }
}
