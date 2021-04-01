package com.github.badpop.jcoinbase;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;

/**
 * A class factory to use to build {@link JCoinbaseClient} objects
 *
 * <p>This class allows you to build new JCoinbaseClient instances by calling the {@link
 * #build(String, String, String, long, boolean)} method
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public abstract class JCoinbaseClientFactory {

  private static JCoinbaseClient instance = null;

  /**
   * Return a new {@link JCoinbaseClient} instance configured with the given parameters. <br>
   * <br>
   *
   * <p><b>You should always use this method to build new JCoinbaseClient objects.</b> <br>
   * <br>
   *
   * <p>Also note that the 'apiKey', 'secret' and 'apiVersion' params are optional so you can set
   * these params to null. However, you will only be able to access the public data of coinbase in
   * this case. So, if you want to access protected data such as accounts or transactions, you
   * should provide an api key and a secret.
   *
   * <p>If you do not already have an api key, please follow this <a
   * href="https://www.coinbase.com/settings/api">link</a> to create a new one
   *
   * <p>Note that you can create a JCoinbaseClient object as a safe thread singleton by passing true
   * for the 'threadSafe' param. This will keep the same instance for each call of this method with
   * 'threadSafe = true', but it is also more resource intensive. If you do not want the
   * JCoinbaseClient to be a thread safe singleton, simply set the 'threadSafe' parameter to false
   * for each call. This will return a new instance each time you call this method
   *
   * @param apiKey your coinbase api key defined in your coinbase account
   * @param secret your coinbase api secret given by coinbase when creating your api key in your
   *     account settings
   * @param apiVersion the api version defined in your coinbase setting. For mor information please
   *     take a look at <a href="https://www.coinbase.com/settings/api">your coinbase api
   *     settings</a>
   * @param timeout the desired timeout for each http request made to the coinbase api in seconds.
   *     The minimum value is 1 second.
   * @param threadSafe define if the returned instance should be a thread safe singleton or not. In
   *     most cases, you can set it to false.
   * @return a new {@link JCoinbaseClient} configured with the given parameters
   */
  public static JCoinbaseClient build(
      final String apiKey,
      final String secret,
      final String apiVersion,
      long timeout,
      final boolean threadSafe) {
    if (timeout < 1) {
      timeout = 3;
      log.warn(
          "The minimum value for timeout is 1 second. The client will use the default timeout defined at 3 seconds instead.");
    }
    return threadSafe
        ? buildThreadSafeSingleton(apiKey, secret, apiVersion, timeout)
        : buildWithoutThreadSafeSingleton(apiKey, secret, apiVersion, timeout);
  }

  private static JCoinbaseClient buildWithoutThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion, long timeout) {
    return new JCoinbaseClient().build(apiKey, secret, apiVersion, timeout, false);
  }

  private static synchronized JCoinbaseClient buildThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion, long timeout) {
    if (instance == null) {
      instance = new JCoinbaseClient().build(apiKey, secret, apiVersion, timeout, true);
    }
    return instance;
  }
}
