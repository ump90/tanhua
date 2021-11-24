package com.tanhua.server.controller;

import com.tanhua.pojo.RecommendUserDto;
import com.tanhua.server.service.TanhuaService;
import com.tanhua.server.service.UserLocationService;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.Constants;
import com.tanhua.vo.NearUserVo;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.TodayBestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@RestController
@RequestMapping("/tanhua")
public class TanhuaController {
  @Autowired private TanhuaService tanhuaService;
  @Autowired private UserLocationService userLocationService;

  @GetMapping("/todayBest")
  public ResponseEntity<Object> getTodayBestVo() {

    TodayBestVo todayBestVo = tanhuaService.getTodayBestVo();
    return ResponseEntity.ok(todayBestVo);
  }

  @GetMapping("/recommendation")
  public ResponseEntity<Object> getRecommendUserPageVo(RecommendUserDto recommendUserDto) {

    PageVo pageVo = tanhuaService.getRecommendUserPageVo(recommendUserDto);
    return ResponseEntity.ok(pageVo);
  }

  @GetMapping("/{id}/personalInfo")
  public ResponseEntity<Object> getUserinfo(@PathVariable("id") Long id) {
    TodayBestVo todayBestVo = tanhuaService.getPersonalInfo(id);
    return ResponseEntity.ok(todayBestVo);
  }

  @GetMapping("/strangerQuestions")
  public ResponseEntity<Object> getQuestions(Long userId) {
    String question = tanhuaService.getStrangerQuestion(userId);
    return ResponseEntity.ok(question);
  }

  @PostMapping("/strangerQuestions")
  public ResponseEntity<Object> replyQuestion(@RequestBody Map<String, String> map) {
    Long userId = Long.parseLong(map.get("userId"));
    String reply = map.get("reply");
    tanhuaService.replyQuestion(userId, reply);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/cards")
  public ResponseEntity<Object> getCards() {
    Long userId = UserThreadLocal.getId();
    return ResponseEntity.ok(tanhuaService.getCards(userId));
  }

  @GetMapping("/{id}/love")
  public ResponseEntity<Object> love(@PathVariable("id") Long id) {
    tanhuaService.like(UserThreadLocal.getId(), id);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/{id}/unlove")
  public ResponseEntity<Object> unlove(@PathVariable("id") Long id) {
    tanhuaService.unlike(UserThreadLocal.getId(), id);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }

  @GetMapping("/search")
  public ResponseEntity<Object> search(@RequestParam String gender, @RequestParam String distance) {
    List<NearUserVo> nearUserVos = userLocationService.getNearByUsers(distance, gender);
    return ResponseEntity.ok(nearUserVos);
  }
}
