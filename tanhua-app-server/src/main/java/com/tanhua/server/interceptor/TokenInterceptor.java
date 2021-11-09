package com.tanhua.server.interceptor;

import com.tanhua.pojo.User;
import com.tanhua.server.utils.UserThreadLocal;
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
    Boolean flag = JwtUtil.verifyToken(token);
    if (flag) {
      Long id = Long.parseLong((String) JwtUtil.getClaims(token).get("id")) ;
      String phone = (String) JwtUtil.getClaims(token).get("phone");
      User user = new User();
      user.setMobile(phone);
      user.setId(id);
      UserThreadLocal.setUser(user);
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
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
