package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.pojo.UserLocation;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@DubboService
@Service
public class UserLocationApiImpl implements UserLocationApi {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void updateLocation(UserLocation userLocation) {
    Query query = new Query(Criteria.where("userId").is(userLocation.getUserId()));
    boolean isSaved = mongoTemplate.exists(query, UserLocation.class);
    if (isSaved) {
      Update update = new Update();
      update
          .set("location", userLocation.getLocation())
          .set("address", userLocation.getAddress())
          .set("updated", System.currentTimeMillis())
          .set("lastUpdated", userLocation.getLastUpdated());
      mongoTemplate.updateFirst(query, update, UserLocation.class);
    } else {
      userLocation.setLastUpdated(System.currentTimeMillis());
      userLocation.setCreated(System.currentTimeMillis());
      userLocation.setUpdated(System.currentTimeMillis());
      mongoTemplate.save(userLocation);
    }
  }

  @Override
  public List<Long> getNearByUser(Long userId, Double distance) {
    Query query = new Query(Criteria.where("userId").is(userId));
    UserLocation userLocation = mongoTemplate.findOne(query, UserLocation.class);
    if (userLocation == null) {
      throw new RuntimeException("没有用户位置信息");
    }
    Distance distance1 = new Distance(distance / 1000, Metrics.KILOMETERS);
    Circle circle = new Circle(userLocation.getLocation(), distance1);
    Query query1 = new Query(Criteria.where("location").withinSphere(circle));
    return CollUtil.getFieldValues(
        mongoTemplate.find(query1, UserLocation.class), "userId", Long.class);
  }
}
