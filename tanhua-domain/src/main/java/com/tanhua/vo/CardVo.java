package com.tanhua.vo;

import lombok.Data;

/**
 * @author UMP90
 * @date 2021/11/15
 */
@Data
public class CardVo {
  private Long id;
  private String avatar;
  private String nickname;
  private String gender;
  private Integer age;
  private String[] tags;
}
