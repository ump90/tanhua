package com.tanhua.server.utils;

import com.tanhua.pojo.ErrorResult;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author UMP90
 * @date 2021/11/24
 */
public class CheckFreezeUserUtil {
  @Autowired RedisTemplate<String, Object> redisTemplate;

  public void check(Long userId) {
    String redisKey = Constants.FROZON_USER_KEY + userId;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
      throw new BusinessException(ErrorResult.freezeError());
    }
  }

  public static CheckFreezeUserUtil newObject() {
    return new CheckFreezeUserUtil();
  }
}
