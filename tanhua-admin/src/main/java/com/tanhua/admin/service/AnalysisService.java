package com.tanhua.admin.service;

import cn.hutool.core.date.DateUtil;
import com.tanhua.pojo.AnalysisByDay;
import com.tanhua.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Service
public class AnalysisService {
  @Autowired LogService logService;

  public void generateStatisticsResult() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String yesterday = DateUtil.yesterday().toString("yyyy-MM-dd");
    String today = simpleDateFormat.format(new Date());
    Long newUser = logService.countUser(today, "0102");
    Long loginUser = logService.countUser(today, "0101");
    Long activeUser = logService.countActiveUser(today);
    Log log = new Log();
    AnalysisByDay analysisByDay = new AnalysisByDay();
  }
}
