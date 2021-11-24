package com.tanhua.server.annotation;

import com.tanhua.enums.LogType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface LogConfig {
  String objectId() default "";

  String key();

  LogType type();
}
