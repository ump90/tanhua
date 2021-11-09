package com.tanhua.server.controller;

import com.tanhua.pojo.Question;
import com.tanhua.pojo.Setting;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.service.UserInfoService;
import com.tanhua.vo.SettingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author UMP90
 * @date 2021/10/31
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserInfoController {
  @Autowired private UserInfoService userInfoService;

  /**
   * 设置头像
   * @param multipartFile 图片
   * @return HTTP200
   * @throws IOException IO错误
   */
  @PostMapping("/header")
  public ResponseEntity<Object> updateUserAvatar(@RequestBody MultipartFile multipartFile)
      throws IOException {
    userInfoService.updateUserAvatar(multipartFile);
    return ResponseEntity.ok("");
  }

  /**
   * 获取用户信息
   * @param id 用户id
   * @return userInfo
   */
  @GetMapping
  public ResponseEntity<Object> getUserInfo(
      @RequestParam(value = "userID", required = false) Long id) {
    UserInfo userInfo = userInfoService.getUserInfo(id);
    return ResponseEntity.ok(userInfo);
  }

  /**
   * 获取默认设置
   * @return HTTP200
   */
  @GetMapping("/settings")
  public ResponseEntity<Object> getDefaultSetting() {
    SettingVo settingVo = userInfoService.getDefaultSetting();
    return ResponseEntity.ok(settingVo);
  }

  /**
   * 更新用户信息
   * @param userInfo 用户信息
   * @return HTTP200
   */
  @PutMapping
  public ResponseEntity<Object> updateUserInfo(@RequestBody UserInfo userInfo) {
    userInfoService.updateUserInfo(userInfo);
    return ResponseEntity.ok("");
  }

  /**
   * 更新用户问题
   * @param question 问题
   * @return HTTP200
   */
  @PutMapping("/questions")
  public ResponseEntity<Object> updateUserQuestion(@RequestBody Question question) {

    userInfoService.updateUserQuestion(question);
    return ResponseEntity.ok("");
  }

  /**
   * 获取通知设置
   * @param setting 通知设置
   * @return HTTP200
   */
  @PostMapping("/notifications/setting")
  public ResponseEntity<Object> updateSetting(@RequestBody Setting setting) {
    userInfoService.updateSetting(setting);
    return ResponseEntity.ok("");
  }

  /**
   * 黑名单 - 移除
   * @param userId 拉黑用户ID
   * @return HTTP200
   */
  @DeleteMapping("/blacklist/{uid}")
  public ResponseEntity<Object> deleteBlackList(@PathVariable(value = "uid") Long userId) {
    userInfoService.deleteBlackList(userId);
    return ResponseEntity.ok("");
  }

  /**
   * 黑名单 - 翻页列表
   *
   * @param page 当前页数
   * @param pageSize 页尺寸
   * @return UserPageVo
   */
  @GetMapping("/blacklist")
  public ResponseEntity<Object> blackListPage(int page, int pageSize) {
    return ResponseEntity.ok(userInfoService.getBlackList(page, pageSize));
  }
}

