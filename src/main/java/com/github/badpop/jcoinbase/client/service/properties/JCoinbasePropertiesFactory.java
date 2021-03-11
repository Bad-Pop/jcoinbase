package com.github.badpop.jcoinbase.client.service.properties;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JCoinbasePropertiesFactory {

  private static JCoinbaseProperties instance = null;

  public static JCoinbaseProperties buildWithoutThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion) {
    return new JCoinbaseProperties().build(apiKey, secret, apiVersion);
  }

  public static synchronized JCoinbaseProperties buildThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion) {
    if (instance == null) {
      instance = new JCoinbaseProperties().build(apiKey, secret, apiVersion);
    }
    return instance;
  }
}
