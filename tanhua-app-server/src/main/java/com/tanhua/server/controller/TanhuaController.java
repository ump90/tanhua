package com.tanhua.server.controller;

import com.tanhua.pojo.RecommendUserDto;
import com.tanhua.server.service.TanhuaService;
import com.tanhua.vo.TodayBestVo;
import com.tanhua.vo.UserPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@RestController
@RequestMapping("/tanhua")
public class TanhuaController {
  @Autowired private TanhuaService tanhuaService;

  @GetMapping("/todayBest")
  public TodayBestVo getTodayBestVo() {
    return tanhuaService.getTodayBestVo();
  }

  @GetMapping("/recommendation")
  public UserPageVo getRecommendUserPageVo(RecommendUserDto recommendUserDto) {
    return tanhuaService.getRecommendUserPageVo(recommendUserDto);
  }

}
