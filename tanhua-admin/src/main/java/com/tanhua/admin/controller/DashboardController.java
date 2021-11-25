package com.tanhua.admin.controller;

import com.tanhua.admin.service.DashBoardService;
import com.tanhua.dto.SummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@RestController("dashboard")
public class DashboardController {
  @Autowired private DashBoardService dashBoardService;

  @GetMapping("/summary")
  public SummaryDto getSummary() {
    return dashBoardService.generateSummary();
  }
}
