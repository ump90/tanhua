package com.tanhua.admin.controller;

import com.tanhua.admin.service.ManageService;
import com.tanhua.dto.FreezeUserDto;
import com.tanhua.vo.PageVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@RestController
@RequestMapping("/manage")
public class ManageController {
  @Autowired private ManageService manageService;

  @GetMapping("/users")
  public PageVo listUsers(@RequestParam Integer page, @RequestParam Integer pagesize) {
    return manageService.listUser(page, pagesize);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<Object> getUserInfoById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(manageService.getUserInfoById(id));
  }

  @GetMapping("/videos")
  public ResponseEntity<Object> getVideoListByUserId(
      @RequestParam Integer page, @RequestParam Integer pagesize, @RequestParam Long uid) {
    return ResponseEntity.ok(manageService.getVideoList(page, pagesize, uid));
  }

  @GetMapping("/messages")
  public ResponseEntity<Object> getAllMessages(
      @RequestParam Integer page,
      @RequestParam Integer pagesize,
      @RequestParam Long uid,
      @RequestParam String state) {

    Integer integerState = null;
    if (StringUtils.isNotBlank(state)) {
      integerState = Integer.parseInt(state);
    }

    return ResponseEntity.ok(manageService.getMessageList(page, pagesize, uid, integerState));
  }

  @PostMapping("/users/freeze")
  public ResponseEntity<Object> freezeUser(@RequestBody FreezeUserDto freezeUserDto) {
    manageService.freezeUser(freezeUserDto);
    Map<String, String> map = new HashMap<>();
    map.put("message", "success");
    return ResponseEntity.ok(map);
  }

  @PostMapping("/users/unfreeze")
  public ResponseEntity<Object> unfreezeUser(@RequestBody Map<String, String> map) {
    Long userId = Long.parseLong(map.get("userId"));
    manageService.unfreezeUser(userId);
    Map<String, String> messageMap = new HashMap<>();
    map.put("message", "success");
    return ResponseEntity.ok(map);
  }
}
