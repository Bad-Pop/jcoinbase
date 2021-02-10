package com.github.badpop.jcoinbase.client;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JCoinbaseClientFactory {

  private static JCoinbaseClient instance = null;

  public static JCoinbaseClient build(final String apiKey, final String secret) {
    return build(apiKey, secret, 3, false);
  }

  /** @param timeout timeout in seconds */
  public static synchronized JCoinbaseClient build(
      final String apiKey, final String secret, final long timeout, final boolean followRedirects) {
    if (instance == null) {
      instance = new JCoinbaseClient().build(apiKey, secret, timeout, followRedirects);
    }
    return instance;
  }
}
