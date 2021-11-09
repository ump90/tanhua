package com.tanhua.dubbo;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@DubboComponentScan("com.tanhua.dubbo.api")
@MapperScan("com.tanhua.dubbo.mapper")
public class DubboApplication {
  public static void main(String[] args) {
      SpringApplication.run(DubboApplication.class,args);
  }
}
