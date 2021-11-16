package com.tanhua.dubbo.api;

import com.tanhua.mongo.UserLike;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author UMP90
 * @date 2021/11/15
 */
@DubboService
public class UserLikeApiImpl implements UserLikeApi {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void save(UserLike userLike) {
    Query query =
        new Query(
            Criteria.where("userId")
                .is(userLike.getUserId())
                .and("likeUserId")
                .is(userLike.getLikeUserId()));

    boolean isSaved = mongoTemplate.exists(query, UserLike.class);
    if (isSaved) {
      Long updateTime = System.currentTimeMillis();
      Update update = new Update();
      update.set("updated", updateTime);
      update.set("isLiked", userLike.getIsLike());
      mongoTemplate.updateFirst(query, update, UserLike.class);
    } else {
      Long createTime = System.currentTimeMillis();
      Long updateTime = System.currentTimeMillis();
      userLike.setCreated(createTime);
      userLike.setUpdated(updateTime);
      mongoTemplate.save(userLike);
    }
  }

  @Override
  public Boolean isLike(Long userId, Long likeUserId) {
    Query query =
        new Query(
            Criteria.where("userId")
                .is(userId)
                .and("likeUserId")
                .is(likeUserId)
                .and("isLike")
                .is(true));
    return mongoTemplate.exists(query, UserLike.class);
  }
}
