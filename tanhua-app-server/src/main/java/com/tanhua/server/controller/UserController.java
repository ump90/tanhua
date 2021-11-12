package com.tanhua.server.controller;

import com.tanhua.pojo.ErrorResult;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * @author UMP90
 * @date 2021/10/29
 */
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired private UserService userService;

  /**
   * 发送验证码
   *
   * @param map 验证码
   * @return HTTP200
   */
  @PostMapping("/login")
  public ErrorResult sendCode(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    return userService.sendSms(phone);
  }

  /**
   * 验证验证码
   *
   * @param map 手机号和验证码
   * @return token
   */
  @PostMapping("/loginVerification")
  public Object loginVerification(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    String code = map.get("verificationCode");
    return userService.login(phone, code);
  }
}
