package com.tanhua.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author UMP90
 * @date 2021/10/30
 */
@Data
@ConfigurationProperties(prefix = "tanhua.oos")
public class OosProperties {
  private String endpoint;
  private String accessKeyId;
  private String accessKeySecret;
  private String bucketName;
  private String key;
  private String url;
}
