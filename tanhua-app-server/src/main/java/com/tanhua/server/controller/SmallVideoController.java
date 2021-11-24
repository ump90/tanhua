package com.tanhua.server.controller;

import com.tanhua.enums.CommentTarget;
import com.tanhua.enums.CommentType;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.SmallVideoService;
import com.tanhua.utils.Constants;
import com.tanhua.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/18
 */
@RestController
@RequestMapping("/smallVideos")
public class SmallVideoController {
  @Autowired private SmallVideoService smallVideoService;
  @Autowired private CommentService commentService;

  @CacheEvict(value = "smallVideo", allEntries = true)
  @PostMapping
  public ResponseEntity<Object> upload(MultipartFile videoThumbnail, MultipartFile videoFile)
      throws IOException {
    smallVideoService.save(videoThumbnail, videoFile);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @Cacheable(value = "smallVideo", key = "#page + '-' + #pageSize")
  @GetMapping
  public PageVo list(@RequestParam Integer page, @RequestParam("pagesize") Integer pageSize) {
    return smallVideoService.list(page, pageSize);
  }

  @PostMapping("/{userId}/userFocus")
  public ResponseEntity<Object> focus(@PathVariable Long userId) {
    smallVideoService.focus(userId, true);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @PostMapping("/{userId}/userUnFocus")
  public ResponseEntity<Object> unfocus(@PathVariable Long userId) {
    smallVideoService.focus(userId, false);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @PostMapping("/comments")
  public ResponseEntity<Object> postComments(@RequestBody Map<String, String> map) {
    String movementId = map.get("movementId");
    String comment = map.get("comment");
    commentService.postComment(movementId, comment, CommentTarget.Video);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/{id}/like")
  public ResponseEntity<Object> like(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, true, commentId, CommentTarget.Video);
    return ResponseEntity.ok(count);
  }

  @GetMapping("/{id}/dislike")
  public ResponseEntity<Object> unlike(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, false, commentId, CommentTarget.Video);
    return ResponseEntity.ok(count);
  }

  @GetMapping("comment/{id}/like")
  public ResponseEntity<Object> commentLike(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, true, commentId, CommentTarget.Comment);
    return ResponseEntity.ok(count);
  }

  @GetMapping("comment/{id}/dislike")
  public ResponseEntity<Object> commentUnlike(@PathVariable(value = "id") String commentId) {
    Integer count =
        commentService.commentAction(CommentType.LIKE, false, commentId, CommentTarget.Comment);
    return ResponseEntity.ok(count);
  }

  @GetMapping("{id}/comments")
  public ResponseEntity<Object> getComments(
      @PathVariable String id,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    PageVo pageVo = commentService.listById(page, pageSize, id);
    return ResponseEntity.ok(pageVo);
  }
}
