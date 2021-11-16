package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class User extends BasePojo implements Serializable {
  private static final long serialVersionUID = -7503907284577076306L;

  @TableId(type = IdType.AUTO)
  private Long id;

  private String mobile;
  private String password;
  private String hxUser;
  private String hxPassword;
}
