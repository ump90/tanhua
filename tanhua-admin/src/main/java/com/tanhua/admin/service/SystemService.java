package com.tanhua.admin.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.tanhua.admin.exception.BusinessException;
import com.tanhua.admin.utils.AdminThreadLocal;
import com.tanhua.dubbo.api.AdminApi;
import com.tanhua.pojo.Admin;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.utils.Constants;
import com.tanhua.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@Service
public class SystemService {
  @Autowired private RedisTemplate<String, Object> redisTemplate;
  @DubboReference private AdminApi adminApi;

  public void getVerificationPicture(String uuid, OutputStream outputStream) {
    ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
    String redisKey = Constants.ADMIN_CHECK_CODE + uuid;
    redisTemplate.opsForValue().set(redisKey, captcha.getCode(), 5, TimeUnit.MINUTES);
    captcha.write(outputStream);
  }

  public String login(String username, String password, String verificationCode, String uuid) {
    String redisKey = Constants.ADMIN_CHECK_CODE + uuid;
    String code = (String) redisTemplate.opsForValue().get(redisKey);
    Admin admin = adminApi.getAdminByName(username);
    String savedPassword = admin.getPassword();

    if (StringUtils.isNotBlank(code)
        && code.equals(verificationCode)
        && StringUtils.isNotBlank(savedPassword)
        && savedPassword.equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
      Map<String, Object> map = new HashMap<>();
      map.put("username", admin.getName());
      map.put("id", admin.getId());
      return JwtUtil.getToken(map);
    } else {
      throw new BusinessException(ErrorResult.loginError());
    }
  }

  public Map<String, String> getUserInfo() {
    Long id = AdminThreadLocal.getAdminId();
    Admin admin = adminApi.getById(id);
    Map<String, String> map = new HashMap<>(3);
    map.put("username", admin.getName());
    map.put("id", admin.getId().toString());
    map.put("avatar", admin.getAvatar());
    return map;
  }
}
