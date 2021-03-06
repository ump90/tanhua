package com.tanhua.server.controller;

import com.tanhua.enums.CommentTarget;
import com.tanhua.enums.CommentType;
import com.tanhua.mongo.Movement;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.MovementService;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import com.tanhua.vo.MovementVo;
import com.tanhua.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@RestController
@RequestMapping("/movements")
public class MovementController {
  @Autowired private MovementService movementService;
  @Autowired private CommentService commentService;

  @PostMapping
  public ResponseEntity<Object> publishMovement(Movement movement, MultipartFile[] files)
      throws IOException {
    movementService.sendMovement(movement, files);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/all")
  public ResponseEntity<Object> getMovementByUserId(
      @RequestParam Long userId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize) {
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No userId");
    }
    PageVo pageVo = movementService.getAllMovementByUserId(userId, page, pageSize);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping
  public ResponseEntity<Object> getMovement(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize) {
    Long userId = UserThreadLocal.getId();
    PageVo pageVo = movementService.getAllMovementOfFriends(userId, page, pageSize);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping("/recommend")
  public ResponseEntity<Object> getRecommendMovement(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize) {
    Long userId = UserThreadLocal.getId();
    PageVo pageVo = movementService.getRecommendMovement(page, pageSize);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getMovementById(@PathVariable String id) {
    MovementVo movementVo = movementService.getSingleMovementById(id);
    return ResponseEntity.ok(movementVo);
  }

  @GetMapping("/visitors")
  public ResponseEntity<Object> getVisitors() {
    return ResponseEntity.ok(movementService.getVisitors());
  }

  @GetMapping("/{id}/like")
  public ResponseEntity<Object> like(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, true, commentId, CommentTarget.Movement);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/dislike")
  public ResponseEntity<Object> unlike(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, false, commentId, CommentTarget.Movement);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/love")
  public ResponseEntity<Object> love(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LOVE, true, commentId, CommentTarget.Movement);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/unlove")
  public ResponseEntity<Object> unlove(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LOVE, false, commentId, CommentTarget.Movement);
    return ResponseEntity.ok(count);
  }
}
