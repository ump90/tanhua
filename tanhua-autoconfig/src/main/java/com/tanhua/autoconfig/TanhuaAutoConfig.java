package com.tanhua.autoconfig;

import com.tanhua.properties.AiFaceProperties;
import com.tanhua.properties.OosProperties;
import com.tanhua.properties.SmsProperties;
import com.tanhua.template.AiFaceTemplate;
import com.tanhua.template.OosTemplate;
import com.tanhua.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@EnableConfigurationProperties({SmsProperties.class, OosProperties.class, AiFaceProperties.class})
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
}
