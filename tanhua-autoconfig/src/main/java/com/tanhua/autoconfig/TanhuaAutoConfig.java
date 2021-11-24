package com.tanhua.autoconfig;

import com.tanhua.properties.*;
import com.tanhua.template.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@EnableConfigurationProperties({
  SmsProperties.class,
  OosProperties.class,
  AiFaceProperties.class,
  HxProperties.class,
  ContentScanProperties.class
})
public class TanhuaAutoConfig {
  @Bean
  public SmsTemplate smsTemplate(SmsProperties smsProperties) {
    return new SmsTemplate(smsProperties);
  }

  @Bean
  public OosTemplate oosTemplate(OosProperties oosProperties) {
    return new OosTemplate(oosProperties);
  }

  @Bean
  public AiFaceTemplate aiFaceTemplate(AiFaceProperties aiFaceProperties) {
    return new AiFaceTemplate(aiFaceProperties);
  }

  @Bean
  @ConditionalOnProperty(prefix = "tanhua.content", value = "enable", havingValue = "true")
  public ContentScanTemplate contentScanTemplate(ContentScanProperties contentScanProperties) {
    return new ContentScanTemplate(contentScanProperties);
  }

  @Bean
  public HxTemplate hxTemplate(HxProperties hxProperties) {
    return new HxTemplate(hxProperties);
  }
}
