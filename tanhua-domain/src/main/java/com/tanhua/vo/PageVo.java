package com.tanhua.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@Data
@Builder
public class PageVo implements Serializable {
  private static final long serialVersionUID = -2043474953132030735L;
  private int counts;
  private int pagesize;
  private int pages;
  private int page;
  private List<?> items;
}
