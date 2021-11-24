package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@Data
public class Admin extends BasePojo {
  private static final long serialVersionUID = -5681848465113561095L;

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;
  private String password;
  private String avatar;
}
