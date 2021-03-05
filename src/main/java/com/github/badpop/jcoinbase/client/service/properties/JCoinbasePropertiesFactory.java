package com.github.badpop.jcoinbase.client.service.properties;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JCoinbasePropertiesFactory {

  private static JCoinbaseProperties instance = null;

  public static JCoinbaseProperties buildWithoutThreadSafeSingleton(
      final String apiKey, final String secret) {
    return new JCoinbaseProperties().build(apiKey, secret);
  }

  public static synchronized JCoinbaseProperties buildThreadSafeSingleton(
      final String apiKey, final String secret) {
    if (instance == null) {
      instance = new JCoinbaseProperties().build(apiKey, secret);
    }
    return instance;
  }
}
