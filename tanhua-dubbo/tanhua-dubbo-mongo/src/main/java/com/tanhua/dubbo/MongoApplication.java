package com.tanhua.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@SpringBootApplication
@EnableDubbo
@EnableAsync
public class MongoApplication {
  public static void main(String[] args) {
    SpringApplication.run(MongoApplication.class, args);
  }
}
