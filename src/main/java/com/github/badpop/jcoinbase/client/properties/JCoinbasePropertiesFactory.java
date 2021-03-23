package com.github.badpop.jcoinbase.client.properties;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * A class factory to use to build {@link JCoinbaseProperties} objects
 *
 * <p>This class allows you to build new JCoinbaseProperties instances by calling the {@link
 * #buildThreadSafeSingleton(String, String, String)} or {@link
 * #buildWithoutThreadSafeSingleton(String, String, String)} methods
 */
@NoArgsConstructor(access = PRIVATE)
public abstract class JCoinbasePropertiesFactory {

  private static JCoinbaseProperties instance = null;

  /**
   * This method build the JCoinbaseClient needed Properties.
   *
   * <p>The 'threadSafe' param define if it should build a new instance or use a thread safe
   * singleton. Note that a thread safe singleton is more resource intensive. You should only use it
   * in some specific cases where multi-threading is crucial
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @param threadSafe boolean defining if the properties should be a thread safe singleton
   * @return a properly configured {@link JCoinbaseProperties}
   */
  public static JCoinbaseProperties build(
      final String apiKey, final String secret, final String apiVersion, final boolean threadSafe) {
    return threadSafe
        ? buildThreadSafeSingleton(apiKey, secret, apiVersion)
        : buildWithoutThreadSafeSingleton(apiKey, secret, apiVersion);
  }

  /**
   * Call this method will return a new JcoinbaseProperties instance
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @return a new configured {@link JCoinbaseProperties}
   */
  private static JCoinbaseProperties buildWithoutThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion) {
    return new JCoinbaseProperties().build(apiKey, secret, apiVersion);
  }

  /**
   * Call this method will return a thread safe singleton of JCoinbaseProperties, but it is more
   * resource intensive than {@link #buildWithoutThreadSafeSingleton(String, String, String)}.
   *
   * <p>You should only use it in some specific cases where multi-threading is crucial
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @return the already computed instance or a new configured {@link JCoinbaseProperties} if not
   *     already computed
   */
  private static synchronized JCoinbaseProperties buildThreadSafeSingleton(
      final String apiKey, final String secret, final String apiVersion) {
    if (instance == null) {
      instance = new JCoinbaseProperties().build(apiKey, secret, apiVersion);
    }
    return instance;
  }
}
