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

  @PostMapping("/login")
  public ErrorResult sendCode(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    return userService.sendSms(phone);
  }

  @PostMapping("/loginVerification")
  public Object loginVerification(@RequestBody Map<String, String> map) {
    String phone = map.get("phone");
    String code = map.get("verificationCode");
    return userService.login(phone, code);
  }
}
