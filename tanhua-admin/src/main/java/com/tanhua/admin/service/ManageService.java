package com.tanhua.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.dto.FreezeUserDto;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.mongo.Movement;
import com.tanhua.mongo.Video;
import com.tanhua.pojo.UserInfo;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.MovementVo;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.VideoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@Service
public class ManageService {
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private VideoApi videoApi;
  @DubboReference private MovementApi movementApi;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public PageVo listUser(Integer page, Integer pageSize) {
    IPage<UserInfo> userInfoPage = userInfoApi.page(new Page<UserInfo>(page, pageSize));
    List<UserInfo> userInfoList = userInfoPage.getRecords();
    for (UserInfo userInfo : userInfoList) {
      String redisKey = Constants.FROZON_USER_KEY + userInfo.getId();
      boolean isBlocked = Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
      if (isBlocked) {
        userInfo.setUserStatus(Constants.USER_STATUS_BLOCKED);

      } else {
        userInfo.setUserStatus(Constants.USER_STATUS_NORMAL);
      }
    }

    return PageVo.builder()
        .counts((int) userInfoPage.getTotal())
        .pagesize(pageSize)
        .page(page)
        .items(userInfoList)
        .pages((int) userInfoPage.getPages())
        .build();
  }

  public UserInfo getUserInfoById(Long id) {
    UserInfo userInfo = userInfoApi.getById(id);
    String redisKey = Constants.FROZON_USER_KEY + userInfo.getId();
    boolean isBlocked = Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    if (isBlocked) {
      userInfo.setUserStatus(Constants.USER_STATUS_BLOCKED);

    } else {
      userInfo.setUserStatus(Constants.USER_STATUS_NORMAL);
    }
    return userInfo;
  }

  public PageVo getVideoList(Integer page, Integer pageSize, Long userId) {
    PageVo pageVo = videoApi.list(page, pageSize, userId);
    List<?> videoList = pageVo.getItems();
    HashSet<Long> userList = new HashSet<>();
    for (Object o : videoList) {
      Video video = (Video) o;
      userList.add(video.getUserId());
    }
    List<UserInfo> userInfoList = userInfoApi.listByIds(userList);
    Map<Long, UserInfo> userInfoHashMap = CollUtil.fieldValueMap(userInfoList, "id");
    List<VideoVo> videoVoList = new ArrayList<>(videoList.size());
    for (Object o : videoList) {
      Video video = (Video) o;
      VideoVo videoVo = VideoVo.init(video, userInfoHashMap.get(video.getUserId()));
      videoVoList.add(videoVo);
    }
    pageVo.setItems(videoVoList);
    return pageVo;
  }

  public PageVo getMessageList(Integer page, Integer pageSize, Long userId, Integer state) {
    Long count = 0L;
    List<Movement> movementList = new ArrayList<>();
    if (state == null) {
      count = movementApi.countByUserId(userId);
      movementList = movementApi.list(userId, page, pageSize);
    } else {
      count = movementApi.countByUserId(userId, state);
      movementList = movementApi.list(userId, page, pageSize, state);
    }

    List<Long> userList = CollUtil.getFieldValues(movementList, "userId", Long.class);
    List<UserInfo> userInfoList = userInfoApi.listByIds(userList);
    Map<Long, UserInfo> userInfoHashMap = CollUtil.fieldValueMap(userInfoList, "id");
    List<MovementVo> movementVoList = new ArrayList<>(movementList.size());
    for (Movement movement : movementList) {
      MovementVo movementVo = MovementVo.init(movement, userInfoHashMap.get(movement.getUserId()));
      movementVoList.add(movementVo);
    }
    return PageVo.builder()
        .pages(PageUtil.convertPage(pageSize, Math.toIntExact(count)))
        .pagesize(pageSize)
        .counts(Math.toIntExact(count))
        .page(page)
        .items(movementVoList)
        .build();
  }

  public void freezeUser(FreezeUserDto freezeUserDto) {
    Long userId = freezeUserDto.getUserId();
    Integer time = freezeUserDto.getFreezingTime();
    String redisKey = Constants.FROZON_USER_KEY + userId;
    String redisValue = JSON.toJSONString(freezeUserDto);
    if (time == 1) {
      time = 3;
    } else if (time == 2) {
      time = 7;
    } else if (time == 3) {
      time = -1;
    }
    redisTemplate.opsForValue().set(redisKey, redisValue, time, TimeUnit.DAYS);
  }

  public void unfreezeUser(Long userId) {
    String redisKey = Constants.FROZON_USER_KEY + userId;
    redisTemplate.delete(redisKey);
  }
}
