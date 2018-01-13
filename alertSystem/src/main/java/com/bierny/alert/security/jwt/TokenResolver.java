package com.bierny.alert.security.jwt;

import javax.servlet.http.HttpServletRequest;

public class TokenResolver {
  public TokenResolver() {
  }

  String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}