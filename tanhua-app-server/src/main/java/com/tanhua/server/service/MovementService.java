package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VisitorApi;
import com.tanhua.mongo.Movement;
import com.tanhua.mongo.Visitor;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.OosTemplate;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.MovementVo;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.TodayBestVo;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Service
public class MovementService {
  @DubboReference private MovementApi movementApi;
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private VisitorApi visitorApi;
  @Autowired private RedisTemplate<String, Object> redisTemplate;
  @Autowired private OosTemplate oosTemplate;

  /**
   * 发布动态
   *
   * @param movement
   * @param files
   * @throws IOException
   */
  public void sendMovement(Movement movement, MultipartFile[] files) throws IOException {
    if (StringUtils.isBlank(movement.getTextContent())) {
      throw new BusinessException(ErrorResult.contentError());
    }
    ArrayList<String> urlList = new ArrayList<>();
    for (MultipartFile file : files) {
      String url = oosTemplate.upload(file.getOriginalFilename(), file.getInputStream());
      urlList.add(url);
    }
    movement.setMedias(urlList);
    movement.setUserId(UserThreadLocal.getId());
    movementApi.publish(movement);
  }

  public PageVo getAllMovementByUserId(Long userId, Integer page, Integer pageSize) {
    List<Movement> movementList = movementApi.list(userId, page, pageSize);
    PageVo pageVo = PageVo.builder().build();
    if (movementList.size() == 0) {
      return pageVo;
    }

    Long counts = movementApi.countByUserId(userId);
    UserInfo userInfo = userInfoApi.getById(userId);
    List<MovementVo> movementVoList = new ArrayList<>();
    movementList.forEach(
        movement -> {
          movementVoList.add(MovementVo.initMovementVo(movement, userInfo));
        });
    pageVo.setItems(movementVoList);
    pageVo.setCounts(Math.toIntExact(counts));
    pageVo.setPage(page);
    pageVo.setPagesize(pageSize);
    pageVo.setPages(PageUtil.convertPage(pageSize, pageVo.getCounts()));

    return pageVo;
  }

  public PageVo getAllMovementOfFriends(Long userId, Integer page, Integer pageSize) {
    List<Movement> movementList = movementApi.listFriends(userId, page, pageSize);
    if (movementList.size() == 0) {
      return PageVo.builder().build();
    }
    ArrayList<MovementVo> movementVoArrayList = convertMovementToVo(movementList);
    int counts = Math.toIntExact(movementApi.countByFriendId(userId));
    int pages = PageUtil.convertPage(pageSize, counts);

    return PageVo.builder()
        .page(page)
        .counts(counts)
        .pagesize(pageSize)
        .pages(pages)
        .items(movementVoArrayList)
        .build();
  }

  public PageVo getRecommendMovement(Integer page, Integer pageSize) {
    String recommendMovementRedisKey = Constants.MOVEMENTS_RECOMMEND + UserThreadLocal.getId();
    String recommendMovementPidString =
        (String) redisTemplate.opsForValue().get(recommendMovementRedisKey);
    List<Movement> movementList = new ArrayList<>();
    if (recommendMovementPidString == null) {
      movementList = movementApi.getRandom(pageSize);
    } else {
      String[] strings = recommendMovementPidString.split(",");
      ArrayList<Long> pidList = new ArrayList<>();
      for (String string : strings) {
        pidList.add(Long.parseLong(string));
      }
      movementList = movementApi.getByPid(pidList);
    }
    if (movementList.size() == 0) {
      return PageVo.builder().build();
    }
    ArrayList<MovementVo> movementVoArrayList = convertMovementToVo(movementList);
    int counts = movementVoArrayList.size();
    int pages = PageUtil.convertPage(pageSize, counts);
    return PageVo.builder()
        .page(page)
        .counts(counts)
        .pagesize(pageSize)
        .pages(pages)
        .items(movementVoArrayList)
        .build();
  }

  public MovementVo getSingleMovementById(String id) {
    Movement movement = movementApi.getById(id);
    UserInfo userInfo = userInfoApi.getById(movement.getUserId());
    return MovementVo.initMovementVo(movement, userInfo);
  }

  private ArrayList<MovementVo> convertMovementToVo(List<Movement> movementList) {
    List<Long> userIdList = CollUtil.getFieldValues(movementList, "userId", Long.class);
    List<UserInfo> userInfoList = userInfoApi.listByIds(userIdList);
    HashMap<Long, UserInfo> userInfoHashMap = new HashMap<>(userInfoList.size());
    for (UserInfo userInfo : userInfoList) {
      userInfoHashMap.put(userInfo.getId(), userInfo);
    }
    ArrayList<MovementVo> movementVoArrayList = new ArrayList<>();
    Long userId = UserThreadLocal.getId();
    for (Movement movement : movementList) {
      MovementVo vo =
          MovementVo.initMovementVo(movement, userInfoHashMap.get(movement.getUserId()));
      String redisKey = Constants.MOVEMENTS_INTERACT_KEY + movement.getId().toHexString();
      String likeHashKey = Constants.MOVEMENT_LIKE_HASHKEY + userId;
      String loveHashKey = Constants.MOVEMENT_LOVE_HASHKEY + userId;
      if (redisTemplate.opsForHash().hasKey(redisKey, likeHashKey)) {
        vo.setHasLiked(1);
      }
      if (redisTemplate.opsForHash().hasKey(redisKey, loveHashKey)) {
        vo.setHasLoved(1);
      }
      movementVoArrayList.add(vo);
    }
    return movementVoArrayList;
  }

  public List<TodayBestVo> getVisitors() {
    Long userId = UserThreadLocal.getId();
    Long lastQueryTime =
        (Long) redisTemplate.opsForValue().get(Constants.USER_LATEST_TIME + userId);
    List<Visitor> visitorList = visitorApi.getVisitorId(userId, lastQueryTime);
    List<Long> userList = CollUtil.getFieldValues(visitorList, "visitorUserId", Long.class);
    List<UserInfo> userInfoList = userInfoApi.listByIds(userList);
    Map<Long, UserInfo> userInfoHashMap = CollUtil.fieldValueMap(userInfoList, "id");
    ArrayList<TodayBestVo> todayBestVos = new ArrayList<>(userInfoList.size());
    for (Visitor visitor : visitorList) {
      TodayBestVo todayBestVo = new TodayBestVo();
      UserInfo userInfo = userInfoHashMap.get(visitor.getVisitorUserId());
      BeanUtils.copyProperties(userInfo, todayBestVo);
      todayBestVo.setTags(userInfo.getTags().split(","));
      todayBestVo.setFateValue(Math.toIntExact(Math.round(visitor.getScore())));
      todayBestVos.add(todayBestVo);
    }
    return todayBestVos;
  }
}
