package com.tanhua.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@Data
@ConfigurationProperties(prefix = "tanhua.sms")
public class SmsProperties {
    private String accessKeyId ;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

}
