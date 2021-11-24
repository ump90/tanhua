package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Data
@TableName("tb_analysis_by_day")
public class AnalysisByDay extends BasePojo {
  private static final long serialVersionUID = -8407031676500518698L;
  private Long id;
  private Date recordDate;
  private Integer numRegistered = 0;
  private Integer numLogin = 0;
  private Integer numActive = 0;
  private Integer numRetention1d = 0;
}
