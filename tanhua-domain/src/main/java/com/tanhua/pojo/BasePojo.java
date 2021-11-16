package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author UMP90
 * @date 2021/10/30
 */
public class BasePojo implements Serializable {
  private static final long serialVersionUID = 7297495510098499808L;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime created;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updated;
}
