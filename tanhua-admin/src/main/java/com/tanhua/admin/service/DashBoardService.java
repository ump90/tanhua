package com.tanhua.admin.service;

import cn.hutool.core.date.DateUtil;
import com.tanhua.admin.mapper.SummaryMapper;
import com.tanhua.dto.SummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Service
public class DashBoardService {
  @Autowired SummaryMapper summaryMapper;

  public SummaryDto generateSummary() {

    String lastMonth = DateUtil.lastMonth().toString("yyyy-MM-dd");
    String lastWeek = DateUtil.lastWeek().toString("yyyy-MM-dd");
    String yesterday = DateUtil.yesterday().toString("yyyy-MM-dd");
    String today = DateUtil.today();
    Integer cumulativeUsers = Math.toIntExact(summaryMapper.countUser());
    Integer activePassMonth = Math.toIntExact(summaryMapper.countActiveUser(lastMonth, today));
    Integer activePassWeek = Math.toIntExact(summaryMapper.countActiveUser(lastWeek, today));
    Integer newUsersToday = Math.toIntExact(summaryMapper.countNewUser(today));
    Integer newUsersYesterday = Math.toIntExact(summaryMapper.countNewUser(yesterday));
    Integer loginUserToday = Math.toIntExact(summaryMapper.countLoginTime(today));
    Integer loginUserYesterday = Math.toIntExact(summaryMapper.countLoginTime(yesterday));

    Integer newUsersTodayRate =
        (int) (((float) (newUsersToday - newUsersYesterday)) / (float) newUsersToday * 100);

    Integer loginTimesTodayRate =
        (int) (((float) (loginUserToday - loginUserYesterday)) / (float) newUsersToday * 100);
    Integer activeUsersToday = Math.toIntExact(summaryMapper.countActiveUser(today, today));
    Integer activeUsersYesterday =
        Math.toIntExact(summaryMapper.countActiveUser(yesterday, yesterday));
    Integer activeUsersTodayRate =
        (int) (((float) (activeUsersToday - activeUsersYesterday)) / (float) newUsersToday * 100);

    return SummaryDto.builder()
        .activePassMonth(activePassMonth)
        .activePassWeek(activePassWeek)
        .activeUsersToday(activeUsersToday)
        .activeUsersTodayRate(activeUsersTodayRate)
        .cumulativeUsers(cumulativeUsers)
        .loginTimesToday(loginUserToday)
        .loginTimesTodayRate(loginTimesTodayRate)
        .newUsersToday(newUsersToday)
        .newUsersTodayRate(newUsersTodayRate)
        .build();
  }
}
