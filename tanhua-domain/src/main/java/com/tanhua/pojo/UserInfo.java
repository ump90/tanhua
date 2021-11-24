package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息表
 *
 * @author UMP90 @TableName tb_user_info
 */
@TableName(value = "tb_user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends BasePojo implements Serializable {
  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  private String nickname;
  private String avatar;
  private String tags;
  private String gender;
  private Integer age;
  private String education;
  private String city;
  private String birthday;
  private String coverPic;
  private String profession;
  private String income;
  private Integer marriage;

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;

  @TableField(exist = false)
  private Integer userStatus;
}
