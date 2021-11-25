package com.tanhua.server.service;

import com.tanhua.pojo.ErrorResult;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Service
public class CheckFreezeUseService {
  @Autowired private RedisTemplate<String, String> redisTemplate;

  public void check() {
    Long userId = UserThreadLocal.getId();

    String redisKey = Constants.FROZON_USER_KEY + userId;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
      throw new BusinessException(ErrorResult.freezeError());
    }
  }

  public static CheckFreezeUseService newObject() {
    return new CheckFreezeUseService();
  }
}
