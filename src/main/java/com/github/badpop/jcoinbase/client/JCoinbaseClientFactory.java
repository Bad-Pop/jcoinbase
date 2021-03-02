package com.github.badpop.jcoinbase.client;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class JCoinbaseClientFactory {

  private static JCoinbaseClient instance = null;

  public static JCoinbaseClient build(
      final String apiKey,
      final String secret,
      long timeout,
      final boolean followRedirects,
      final boolean threadSafe) {
    if (timeout < 1) {
      timeout = 3;
      notifyDefaultTimeout();
    }
    return threadSafe
        ? buildThreadSafeSingleton(apiKey, secret, timeout, followRedirects)
        : buildWithoutThreadSafeSingleton(apiKey, secret, timeout, followRedirects);
  }

  public static JCoinbaseClient buildWithoutThreadSafeSingleton(
      final String apiKey, final String secret, long timeout, final boolean followRedirects) {
    if (timeout < 1) {
      timeout = 3;
      notifyDefaultTimeout();
    }
    return new JCoinbaseClient().build(apiKey, secret, timeout, followRedirects, false);
  }

  public static synchronized JCoinbaseClient buildThreadSafeSingleton(
      final String apiKey, final String secret, long timeout, final boolean followRedirects) {
    if (instance == null) {
      if (timeout < 1) {
        timeout = 3;
        notifyDefaultTimeout();
      }
      instance = new JCoinbaseClient().build(apiKey, secret, timeout, followRedirects, true);
    }
    return instance;
  }

  private static void notifyDefaultTimeout() {
    log.warn(
        "The minimum value for timeout is 1 second. The client will use the default timeout defined at 3 seconds instead.");
  }
}
