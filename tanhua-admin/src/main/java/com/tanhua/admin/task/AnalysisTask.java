package com.tanhua.admin.task;

import com.tanhua.admin.service.AnalysisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Component
public class AnalysisTask {
  private AnalysisService analysisService;

  @Scheduled(cron = "0 55 23 * * *")
  public void dailyTask() {
    analysisService.generateStatisticsResult();
  }
}
