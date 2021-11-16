package com.tanhua.server.controller;

import com.tanhua.server.service.CommentService;
import com.tanhua.utils.Constants;
import com.tanhua.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/11
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
  @Autowired private CommentService commentService;

  @GetMapping
  public ResponseEntity<Object> getComments(
      String movementId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    PageVo pageVo = commentService.listByMovementId(page, pageSize, movementId);
    return ResponseEntity.ok(pageVo);
  }

  @PostMapping
  public ResponseEntity<Object> postComments(@RequestBody Map<String, String> map) {
    String movementId = map.get("movementId");
    String comment = map.get("comment");
    commentService.postComment(movementId, comment);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/{id}/like")
  public ResponseEntity<Object> like(@PathVariable(value = "id") String commentId) {
    Integer count = commentService.like(commentId);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/dislike")
  public ResponseEntity<Object> unlike(@PathVariable(value = "id") String commentId) {
    Integer count = commentService.unlike(commentId);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/love")
  public ResponseEntity<Object> love(@PathVariable(value = "id") String commentId) {
    Integer count = commentService.love(commentId);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/unlove")
  public ResponseEntity<Object> unlove(@PathVariable(value = "id") String commentId) {
    Integer count = commentService.unlove(commentId);
    return ResponseEntity.ok(count);
  }
}
