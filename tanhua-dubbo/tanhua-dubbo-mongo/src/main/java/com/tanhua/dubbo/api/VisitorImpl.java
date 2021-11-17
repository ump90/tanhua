package com.tanhua.dubbo.api;

import com.tanhua.mongo.Visitor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@DubboService
public class VisitorImpl implements VisitorApi {
  private MongoTemplate mongoTemplate;

  @Override
  public List<Visitor> getVisitorId(Long userId, Long lastTime) {
    if (lastTime != null) {
      Query query = new Query(Criteria.where("userId").is(userId).and("date").gt(lastTime));
      query.with(Sort.by(Sort.Order.desc("date")));
      return mongoTemplate.find(query, Visitor.class);
    } else {
      Query query = new Query(Criteria.where("userId").is(userId));
      query.with(Sort.by(Sort.Order.desc("date"))).limit(5);
      return mongoTemplate.find(query, Visitor.class);
    }
  }

  @Override
  public void saveVisitor(Visitor visitor) {
    mongoTemplate.save(visitor);
  }

  @Override
  public Boolean isVisited(Long userId, String dataString) {
    Query query =
        new Query(Criteria.where("visitorUserId").is(userId).and("visitDate").is(dataString));
    return mongoTemplate.exists(query, Visitor.class);
  }
}
