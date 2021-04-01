package com.github.badpop.jcoinbase.service.utils;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;

public interface AuthenticationUtils {

  static String[] getHeaders(
      final AuthenticationService authenticationService,
      final JCoinbaseClient client,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {
    return authenticationService.getAuthenticationHeaders(
        client.getProperties(), httpMethod, httpPath, httpBody);
  }
}
