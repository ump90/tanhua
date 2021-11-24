package com.tanhua.admin.controller;

import com.tanhua.admin.service.SystemService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@RestController
@RequestMapping("/system")
public class SystemController {
  @Autowired SystemService systemService;

  @GetMapping("/users/verification")
  public void getVerificationPicture(@RequestParam String uuid, HttpServletResponse response)
      throws IOException {
    // 设置响应参数
    response.setDateHeader("Expires", 0);
    // Set standard HTTP/1.1 no-cache headers.
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    // Set standard HTTP/1.0 no-cache header.
    response.setHeader("Pragma", "no-cache");
    response.setContentType("image/jpeg");
    systemService.getVerificationPicture(uuid, response.getOutputStream());
  }

  public ResponseEntity<Object> login(@RequestBody Map<String, String> params) {
    String username = params.get("username");
    String password = params.get("password");
    String verificationCode = params.get("verificationCode");
    String uuid = params.get("uuid");
    if (StringUtils.isBlank(username)
        || StringUtils.isBlank(password)
        || StringUtils.isBlank(verificationCode)
        || StringUtils.isBlank(uuid)) {
      return ResponseEntity.badRequest().body("参数不能为空");
    }
    String token = systemService.login(username, password, verificationCode, uuid);
    if (StringUtils.isBlank(token)) {
      return ResponseEntity.badRequest().body("登录失败");
    }
    Map<String, String> map = new HashMap<>(1);
    map.put("token", token);
    return ResponseEntity.ok(map);
  }

  @GetMapping("/users/profile")
  public ResponseEntity<Object> getUserInfo() {
    return ResponseEntity.ok(systemService.getUserInfo());
  }
}
