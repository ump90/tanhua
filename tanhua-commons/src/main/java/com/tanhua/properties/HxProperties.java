package com.tanhua.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author UMP90
 * @date 2021/11/12
 */
@Data
@ConfigurationProperties(prefix = "tanhua.hx")
public class HxProperties {
  private String appKey;
  private String clientId;
  private String clientSecret;
}
