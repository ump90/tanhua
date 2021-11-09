package com.tanhua.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author UMP90
 * @date 2021/10/31
 */
@Data
@ConfigurationProperties(prefix = "tanhua.aip")
public class AiFaceProperties {
    private String appId ;
    private String appKey;
    private String appSecretKey;
}
