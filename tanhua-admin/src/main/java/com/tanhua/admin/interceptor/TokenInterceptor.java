package com.tanhua.admin.interceptor;

import com.tanhua.admin.utils.AdminThreadLocal;
import com.tanhua.pojo.Admin;
import com.tanhua.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author UMP90
 * @date 2021/11/3
 */
public class TokenInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String token = request.getHeader("Authorization");
    boolean flag = false;
    try {
      flag = JwtUtil.verifyToken(token);
    } catch (Exception e) {
      response.sendError(401);
      return false;
    }

    if (flag) {
      Long id = Long.parseLong((String) JwtUtil.getClaims(token).get("id"));
      String username = (String) JwtUtil.getClaims(token).get("username");
      Admin admin = new Admin();
      admin.setId(id);
      admin.setName(username);
      AdminThreadLocal.setAdmin(admin);
      return true;

    } else {
      response.sendError(401);
      return false;
    }
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    AdminThreadLocal.remove();
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
