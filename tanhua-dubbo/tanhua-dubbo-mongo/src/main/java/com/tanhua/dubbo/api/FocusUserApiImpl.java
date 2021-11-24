package com.tanhua.dubbo.api;

import com.tanhua.mongo.FocusUser;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@DubboService
@Service
public class FocusUserApiImpl implements FocusUserApi {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void focus(Long userId, Long followedUserId, boolean save) {
    if (save) {
      FocusUser focusUser = new FocusUser();
      focusUser.setUserId(userId);
      focusUser.setFollowedUserId(followedUserId);
      focusUser.setCreated(System.currentTimeMillis());
      mongoTemplate.save(focusUser);
    } else {
      Query query =
          new Query(Criteria.where("userId").is(userId).and("followedUserId").is(followedUserId));
      mongoTemplate.remove(query, FocusUser.class);
    }
  }
}
