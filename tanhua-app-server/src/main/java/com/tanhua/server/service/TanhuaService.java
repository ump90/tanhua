package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.*;
import com.tanhua.mongo.UserLike;
import com.tanhua.pojo.RecommendUser;
import com.tanhua.pojo.RecommendUserDto;
import com.tanhua.pojo.User;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.HxTemplate;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.CardVo;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.TodayBestVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@Service
public class TanhuaService {
  @DubboReference private RecommendUserApi recommendUserApi;
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private QuestionApi questionApi;
  @DubboReference private UserApi userApi;
  @DubboReference private UserLikeApi userLikeApi;
  @Autowired private HxTemplate hxTemplate;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private MessageService messageService;

  public TodayBestVo getTodayBestVo() {
    Long userId = UserThreadLocal.getId();
    RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(userId);
    UserInfo userInfo = userInfoApi.getById(recommendUser.getUserId());
    TodayBestVo todayBestVo = new TodayBestVo();
    BeanUtils.copyProperties(userInfo, todayBestVo);
    String tagString = userInfo.getTags();
    if (tagString != null) {
      String[] tags = tagString.split(".");
      todayBestVo.setTags(tags);
    }
    todayBestVo.setFateValue(recommendUser.getScore().intValue());
    return todayBestVo;
  }

  public PageVo getRecommendUserPageVo(RecommendUserDto recommendUserDto) {
    Long userId = UserThreadLocal.getId();
    int page = recommendUserDto.getPage();
    int pageSize = recommendUserDto.getPagesize();
    List<RecommendUser> recommendUserList =
        recommendUserApi.queryWithMaxScoreList(userId, pageSize, page);
    int totalCount = Math.toIntExact(recommendUserApi.count(userId));
    List<Long> userIdList = new ArrayList<>();
    for (RecommendUser recommendUser : recommendUserList) {
      userIdList.add(recommendUser.getUserId());
    }

    List<UserInfo> userInfoList = userInfoApi.listByIds(userIdList);
    Map<Long, UserInfo> userInfoMap = CollUtil.fieldValueMap(userInfoList, "id");
    List<TodayBestVo> todayBestVoList = new ArrayList<>();
    for (RecommendUser recommendUser : recommendUserList) {
      TodayBestVo todayBestVo = new TodayBestVo();
      UserInfo userInfo = userInfoMap.get(recommendUser.getUserId());
      BeanUtils.copyProperties(userInfo, todayBestVo);
      todayBestVo.setFateValue(recommendUser.getScore().intValue());
      todayBestVo.setTags(userInfo.getTags().split("."));
      todayBestVoList.add(todayBestVo);
    }

    return PageVo.builder()
        .items(todayBestVoList)
        .pages(PageUtil.convertPage(pageSize, totalCount))
        .counts(totalCount)
        .page(page)
        .pagesize(pageSize)
        .build();
  }

  public TodayBestVo getPersonalInfo(Long userId) {
    UserInfo userInfo = userInfoApi.getById(userId);
    TodayBestVo todayBestVo = new TodayBestVo();
    BeanUtils.copyProperties(userInfo, todayBestVo);
    String tagString = userInfo.getTags();
    if (tagString != null) {
      String[] tags = tagString.split(".");
      todayBestVo.setTags(tags);
    }
    Long toUserId = UserThreadLocal.getId();
    Integer fateValue = recommendUserApi.query(userId, toUserId).getScore().intValue();
    todayBestVo.setFateValue(fateValue);
    return todayBestVo;
  }

  public String getStrangerQuestion(Long userId) {
    return questionApi.getQuestionByUserId(userId).getTxt();
  }

  public void replyQuestion(Long userId, String reply) {
    Long fromUserId = UserThreadLocal.getId();
    User fromUser = userApi.getById(fromUserId);
    User toUser = userApi.getById(userId);
    hxTemplate.sendMessage(fromUser.getHxUser(), toUser.getHxUser(), reply);
  }

  public List<CardVo> getCards(Long userId) {
    List<RecommendUser> recommendUserList = recommendUserApi.queryCards(userId, 10);
    List<UserInfo> userInfoList =
        userInfoApi.listByIds(CollUtil.getFieldValues(recommendUserList, "userId", Long.class));
    Map<Long, UserInfo> userInfoMap = CollUtil.fieldValueMap(userInfoList, "id");
    ArrayList<CardVo> cardVos = new ArrayList<>();
    for (RecommendUser user : recommendUserList) {
      CardVo cardVo = new CardVo();
      UserInfo userinfo = userInfoMap.get(user.getUserId());
      BeanUtils.copyProperties(userinfo, cardVo);
      cardVo.setTags(userinfo.getTags().split(","));
      cardVos.add(cardVo);
    }
    return cardVos;
  }

  public void like(Long userId, Long likeUserId) {
    UserLike userLike = new UserLike();
    userLike.setUserId(userId);
    userLike.setIsLike(true);
    userLike.setLikeUserId(likeUserId);
    userLikeApi.save(userLike);
    redisTemplate.opsForSet().add(Constants.USER_LOVE_KEY + userId, likeUserId.toString());
    redisTemplate.opsForSet().remove(Constants.USER_UNLOVED_KEY + userId, likeUserId.toString());
    addContract(userId, likeUserId);
  }

  public void unlike(Long userId, Long unlikeUserId) {
    UserLike userLike = new UserLike();
    userLike.setUserId(userId);
    userLike.setIsLike(false);
    userLike.setLikeUserId(unlikeUserId);
    userLikeApi.save(userLike);
    redisTemplate.opsForSet().remove(Constants.USER_LOVE_KEY + userId, unlikeUserId.toString());
    redisTemplate.opsForSet().add(Constants.USER_UNLOVED_KEY + userId, unlikeUserId.toString());
  }

  @Async
  public void addContract(Long userId, Long likeUserId) {
    String key1 = Constants.USER_LOVE_KEY + userId;
    String key2 = Constants.USER_LOVE_KEY + likeUserId;
    if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key1, likeUserId.toString()))
        && Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key2, userId.toString()))) {
      messageService.addContract(likeUserId, userId);
      return;
    }
    if (userLikeApi.isLike(userId, likeUserId) && userLikeApi.isLike(likeUserId, userId)) {
      messageService.addContract(likeUserId, userId);
    }
  }
}
