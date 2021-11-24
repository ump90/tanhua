package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@Data
@TableName("tb_announcement")
public class Announcement extends BasePojo {
  private static final long serialVersionUID = 6738881043629177774L;

  @TableId(type = IdType.AUTO)
  private Long id;

  private String title;
  private String description;
}
