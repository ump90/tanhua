package com.tanhua.server.service;

import com.tanhua.dubbo.api.UserApi;
import com.tanhua.enums.LogType;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.User;
import com.tanhua.server.annotation.LogConfig;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.CheckFreezeUserUtil;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.HxTemplate;
import com.tanhua.template.SmsTemplate;
import com.tanhua.utils.Constants;
import com.tanhua.utils.JwtUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@Service
public class UserService {
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private SmsTemplate smsTemplate;
  @DubboReference private UserApi userApi;
  @Autowired private HxTemplate hxTemplate;

  // 发送短信

  public ErrorResult sendSms(String phone) {
    String redisKey = Constants.PHONE_NUMBER + phone;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
      return ErrorResult.smsNotExpired();
    } else {
      try {
        String code = RandomStringUtils.randomNumeric(6);
        smsTemplate.sendSmsTest(phone, code);
        redisTemplate.opsForValue().set(redisKey, code, 5L, TimeUnit.MINUTES);
        return null;
      } catch (Exception e) {
        e.printStackTrace();
        return ErrorResult.fail();
      }
    }
  }

  // 登陆验证
  @LogConfig(type = LogType.LOGIN, key = "user", objectId = "#phone")
  public Map<String, String> login(String phone, String code) {
    String redisKey = Constants.PHONE_NUMBER + phone;
    boolean isNewUser = false;
    User user = userApi.getByPhone(phone);
    if (user == null) {
      registerNewUser(phone);
      isNewUser = true;
    }

    if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
      Long userId = userApi.getByPhone(phone).getId();
      CheckFreezeUserUtil.newObject().check(userId);

      String savedCode = redisTemplate.opsForValue().get(redisKey);

      if (code.equals(savedCode)) {
        Map<String, String> tokenMap = new HashMap<>(2);
        tokenMap.put("phone", phone);
        tokenMap.put("id", String.valueOf(userId));
        String token = JwtUtil.getToken(tokenMap);
        Map<String, String> returnMap = new HashMap<>(2);
        returnMap.put("token", token);
        returnMap.put("isNew", String.valueOf(isNewUser));
        redisTemplate.delete(redisKey);
        return returnMap;
      } else {
        throw new BusinessException(ErrorResult.loginCheckError());
      }
    } else {
      throw new BusinessException(ErrorResult.loginError());
    }
  }

  public User getById(Long userId) {
    return userApi.getById(userId);
  }

  // 修改手机号

  public void changePhone(String phone) {
    Long userId = UserThreadLocal.getId();
    User user = this.getById(userId);
    if (phone.equals(user.getMobile())) {
      return;
    }
    user.setMobile(phone);
    userApi.save(user);
  }

  @LogConfig(type = LogType.REGISTER, key = "user", objectId = "#phone")
  private void registerNewUser(String phone) {
    User newUser = new User();
    newUser.setMobile(phone);
    newUser.setPassword(
        DigestUtils.md5DigestAsHex(Constants.INIT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
    userApi.save(newUser);
    User savedUser = userApi.getByPhone(phone);
    String hxUsername = Constants.HX_USER_PREFIX + savedUser.getId();
    savedUser.setHxUser(hxUsername);
    savedUser.setHxPassword(Constants.INIT_PASSWORD);
    userApi.updateById(savedUser);
    hxTemplate.createUser(hxUsername, Constants.INIT_PASSWORD);
  }
}
