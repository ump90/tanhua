package com.tanhua.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Data
@Builder
public class FreezeUserDto implements Serializable {
  private static final long serialVersionUID = -4080954637500862675L;
  private Long userId;
  private Integer freezingTime;
  private Integer freezingRange;
  private String reasonsForFreezing;
  private String frozenRemarks;
}
