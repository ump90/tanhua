package com.tanhua.admin.service;

import cn.hutool.core.date.DateUtil;
import com.tanhua.pojo.AnalysisByDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Service
@Slf4j
public class AnalysisService {
  @Autowired LogService logService;
  @Autowired AnalysisByDayService analysisByDayService;

  public void generateStatisticsResult() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String yesterday = DateUtil.yesterday().toString("yyyy-MM-dd");
    String today = simpleDateFormat.format(new Date());
    Long newUser = logService.countUser(today, "0102");
    Long loginUser = logService.countUser(today, "0101");
    Long activeUser = logService.countActiveUser(today);
    Long retentionUser = logService.countRetentionUser(today, yesterday);
    AnalysisByDay analysisByDay =
        AnalysisByDay.builder()
            .numActive(activeUser.intValue())
            .numLogin(loginUser.intValue())
            .numRegistered(retentionUser.intValue())
            .numRetention1d(retentionUser.intValue())
            .recordDate(new Date())
            .build();
    analysisByDayService.save(analysisByDay);
    log.info("今日日志已生成");
  }


}
