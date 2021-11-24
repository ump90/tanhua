package com.tanhua.admin.config;

import com.tanhua.admin.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry interceptorRegistry) {
    interceptorRegistry
        .addInterceptor(new TokenInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/user/login*");
  }
}
