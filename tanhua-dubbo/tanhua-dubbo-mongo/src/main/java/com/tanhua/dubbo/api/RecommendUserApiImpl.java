package com.tanhua.dubbo.api;

import com.tanhua.mongo.UserLike;
import com.tanhua.pojo.RecommendUser;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@DubboService
@Service
public class RecommendUserApiImpl implements RecommendUserApi {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public RecommendUser queryWithMaxScore(Long toUserId) {
    Query query = new Query(Criteria.where("toUserId").is(toUserId));
    Sort sort = Sort.by(Sort.Order.desc("Score"));
    query.with(sort);
    return mongoTemplate.findOne(query, RecommendUser.class);
  }

  @Override
  public List<RecommendUser> queryWithMaxScoreList(Long toUserId, int pageSize, int pageNum) {
    Query query = new Query(Criteria.where("toUserId").is(toUserId));
    Sort sort = Sort.by(Sort.Order.desc("Score"));
    query.with(sort);
    query.limit(pageSize);
    query.skip((long) (pageNum - 1) * pageSize);
    return mongoTemplate.find(query, RecommendUser.class);
  }

  @Override
  public Long count(Long toUserId) {
    Query query = new Query(Criteria.where("toUserId").is(toUserId));
    return mongoTemplate.count(query, RecommendUser.class);
  }

  @Override
  public RecommendUser query(Long userId, Long toUserId) {
    Query query = new Query(Criteria.where("userId").is(userId).and("toUserId").is(toUserId));
    return mongoTemplate.findOne(query, RecommendUser.class);
  }

  @Override
  public List<RecommendUser> queryCards(Long toUserId, int size) {
    Query query = new Query(Criteria.where("userId").is(toUserId));
    List<Long> notWantedUserIds =
        mongoTemplate.find(query, UserLike.class).stream()
            .map(UserLike::getLikeUserId)
            .collect(java.util.stream.Collectors.toList());

    Criteria criteria = new Criteria();
    criteria.and("toUserId").is(toUserId).and("userId").nin(notWantedUserIds);
    TypedAggregation<RecommendUser> aggregation =
        Aggregation.newAggregation(
            RecommendUser.class, Aggregation.match(criteria), Aggregation.sample(size));
    return mongoTemplate.aggregate(aggregation, RecommendUser.class).getMappedResults();
  }
}
