package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class User extends BasePojo implements Serializable {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String mobile;
  private String hxUser;
  private String hxPassword;
}
