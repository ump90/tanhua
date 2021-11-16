package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@Data
@TableName("tb_settings")
public class Setting extends BasePojo implements Serializable {
  private static final long serialVersionUID = 1042486408145611828L;

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long userId;
  private Integer likeNotification;
  private Integer pinglunNotification;
  private Integer gonggaoNotification;
}
