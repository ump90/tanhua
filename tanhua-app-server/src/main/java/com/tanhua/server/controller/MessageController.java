package com.tanhua.server.controller;

import com.tanhua.enums.CommentType;
import com.tanhua.server.service.AnnouncementService;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.MessageService;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import com.tanhua.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/13
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
  @Autowired private MessageService messageService;
  @Autowired private AnnouncementService announcementService;
  @Autowired private CommentService commentService;

  @GetMapping("/contacts")
  public ResponseEntity<Object> getContractList(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
      @RequestParam String keyword) {
    PageVo pageVo = messageService.getContractList(page, pageSize, keyword);
    return ResponseEntity.ok(pageVo);
  }

  @PostMapping("/contacts")
  public ResponseEntity<Object> addContract(@RequestBody Map<String, Integer> requestMap) {
    Long friendUserId = Long.valueOf(requestMap.get("userId"));
    Long userId = UserThreadLocal.getId();
    messageService.addContract(friendUserId, userId);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/announcements")
  public ResponseEntity<Object> listAnnouncement(
      @RequestParam Integer page, @RequestParam("pagesize") Integer pageSize) {
    PageVo pageVo = announcementService.list(page, pageSize);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping("/likes")
  public ResponseEntity<Object> getLikes(Integer page, Integer pagesize) {
    PageVo pageVo = commentService.getLovesOrLikes(page, pagesize, CommentType.LIKE);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping("/loves")
  public ResponseEntity<Object> getLoves(Integer page, Integer pagesize) {
    PageVo pageVo = commentService.getLovesOrLikes(page, pagesize, CommentType.LOVE);
    return ResponseEntity.ok(pageVo);
  }
}
