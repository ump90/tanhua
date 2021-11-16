package com.tanhua.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author UMP90
 * @date 2021/11/13
 */
@Data
@Builder
public class ContractVo {
  private Integer id;
  private String userId;
  private String avatar;
  private String nickname;
  private String gender;
  private Integer age;
  private String city;
}
