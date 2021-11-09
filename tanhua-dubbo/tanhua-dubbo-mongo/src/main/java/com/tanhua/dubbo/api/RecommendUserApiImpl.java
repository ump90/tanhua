package com.tanhua.dubbo.api;

import com.tanhua.pojo.RecommendUser;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@DubboService
public class RecommendUserApiImpl implements RecommendUserApi {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public RecommendUser queryWithMaxScore(Long toUserId) {
    Query query = new Query(Criteria.where("toUserId").is(toUserId));
    Sort sort = Sort.by(Sort.Order.desc("Score"));
    query.with(sort);
    return mongoTemplate.findOne(query,RecommendUser.class);
  }

  @Override
  public List<RecommendUser> queryWithMaxScoreList(Long toUserId, int pageSize, int pageNum) {
    Query query = new Query(Criteria.where("toUserId").is(toUserId));
    Sort sort = Sort.by(Sort.Order.desc("Score"));
    query.with(sort);
    query.limit(pageSize);
    query.skip((long) (pageNum - 1) * pageSize);
    return mongoTemplate.find(query,RecommendUser.class);
  }

  @Override
  public Long queryCount(Long toUserId) {
    Query query=new Query(Criteria.where("toUserId").is(toUserId));
    return mongoTemplate.count(query,RecommendUser.class);
  }
}
