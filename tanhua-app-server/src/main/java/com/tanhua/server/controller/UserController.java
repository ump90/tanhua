package com.tanhua.server.controller;

import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.User;
import com.tanhua.server.service.UserService;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/**
 * @author UMP90
 * @date 2021/10/29
 */
@RestController
public class UserController {
  @Autowired private UserService userService;

  /**
   * 发送验证码
   *
   * @param map 验证码
   * @return HTTP200
   */
  @PostMapping("/user/login")
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
  @PostMapping("/user/loginVerification")
  public ResponseEntity<Object> loginVerification(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    String code = map.get("verificationCode");
    Map<String, String> resultMap = userService.login(phone, code);
    return ResponseEntity.ok(resultMap);
  }

  @GetMapping("/huanxin/user")
  public ResponseEntity<Object> hxUserInfo() {
    Long userId = UserThreadLocal.getId();
    User user = userService.getById(userId);
    Map<String, String> infoMap = new HashMap<>(2);
    infoMap.put("username", user.getHxUser());
    infoMap.put("password", user.getHxPassword());
    return ResponseEntity.ok(infoMap);
  }

  @PostMapping("/phone")
  public ResponseEntity<Object> changePhone(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    userService.changePhone(phone);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }
}
