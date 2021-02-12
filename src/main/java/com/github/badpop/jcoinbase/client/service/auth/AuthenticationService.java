package com.github.badpop.jcoinbase.client.service.auth;

import org.apache.commons.codec.digest.HmacUtils;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

public class AuthenticationService {

  public String[] getAuthenticationHeaders(
      final String apiKey,
      final String secret,
      final long timestamp,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {

    var message = timestamp + httpMethod + httpPath + httpBody;
    var signature = new HmacUtils(HMAC_SHA_256, secret.getBytes()).hmacHex(message);

    return new String[] {
      "CB-ACCESS-SIGN",
      signature,
      "CB-ACCESS-TIMESTAMP",
      String.valueOf(timestamp),
      "CB-ACCESS-KEY",
      apiKey,
      "Accept",
      "application/json"
    };
  }
}
