package com.tanhua.admin.task;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author UMP90
 * @date 2021/11/23
 */
public class AnalysisTask {
  @Scheduled(cron = "0 8 * * *")
  public void dailyTask() {}
}
