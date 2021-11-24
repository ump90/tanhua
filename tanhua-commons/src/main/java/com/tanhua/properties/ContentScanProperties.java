package com.tanhua.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author UMP90
 * @date 2021/10/30
 */
@Data
@ConfigurationProperties(prefix = "tanhua.content")
public class ContentScanProperties {

  private String accessKeyId;
  private String accessKeySecret;
  private String scenes;
}
