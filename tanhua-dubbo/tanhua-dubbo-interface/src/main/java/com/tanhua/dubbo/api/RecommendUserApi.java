package com.tanhua.dubbo.api;

import com.tanhua.pojo.RecommendUser;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/6
 */
public interface RecommendUserApi {
  RecommendUser queryWithMaxScore(Long toUserId);

  List<RecommendUser> queryWithMaxScoreList(Long toUserId, int pageSize, int pageNum);

  Long count(Long toUserId);

  RecommendUser query(Long userId, Long toUserId);

  List<RecommendUser> queryCards(Long toUserId, int size);
}
