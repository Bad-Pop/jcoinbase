package com.github.badpop.jcoinbase.properties;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JCoinbasePropertiesFactory {

  private static JCoinbaseProperties instance = null;

  public static synchronized JCoinbaseProperties getProperties(
      final String apiKey, final String secret) {
    if (instance == null) {
      instance = new JCoinbaseProperties().build(apiKey, secret);
    }
    return instance;
  }
}
