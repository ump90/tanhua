package com.tanhua.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Data
@Builder
public class SummaryDto implements Serializable {
  private static final long serialVersionUID = 7635720607537204837L;
  private Integer cumulativeUsers;
  private Integer activePassMonth;
  private Integer activePassWeek;
  private Integer newUsersToday;
  private Integer newUsersTodayRate;
  private Integer loginTimesToday;
  private Integer loginTimesTodayRate;
  private Integer activeUsersToday;
  private Integer activeUsersTodayRate;
}
