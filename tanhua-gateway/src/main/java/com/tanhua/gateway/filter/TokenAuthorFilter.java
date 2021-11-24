package com.tanhua.gateway.filter;

import com.tanhua.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author UMP90
 * @date 2021/11/20
 */
@Slf4j
public class TokenAuthorFilter implements GlobalFilter, Ordered {
  @Value("${gateway.excludedUrls}")
  public String excludeUrlString;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String url = exchange.getRequest().getURI().getPath();
    String[] excludeUrls = excludeUrlString.split(",");
    log.info("url:{}", url);
    for (String excludeUrl : excludeUrls) {
      if (excludeUrl.equals(url)) {
        return chain.filter(exchange);
      }
    }
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    boolean checkTokenResult = JwtUtil.verifyToken(token);
    if (!checkTokenResult) {
      log.info("token:{}", token);
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
