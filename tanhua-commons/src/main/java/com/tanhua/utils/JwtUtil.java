package com.tanhua.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/10/30
 */
public class JwtUtil {
  private static final int TOKEN_EXPIRATION_TIME = 3600 * 1000;
  private static final String TOKEN_KEY = "asswecan";

  public static String getToken(Map map) {
    long currentTime = System.currentTimeMillis();
    return Jwts.builder()
        .addClaims(map)
        .signWith(SignatureAlgorithm.HS512, TOKEN_KEY)
        .setExpiration(new Date(currentTime + TOKEN_EXPIRATION_TIME))
        .compact();
  }

  public static Claims getClaims(String token) {
    return Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token).getBody();
  }

  public static Boolean verifyToken(String token) {
    if (StringUtils.isEmpty(token)) {
      return false;
    }

    try {
      Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
