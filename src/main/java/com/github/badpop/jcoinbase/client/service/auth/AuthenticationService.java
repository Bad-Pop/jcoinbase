package com.github.badpop.jcoinbase.client.service.auth;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.utils.StringUtils;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.codec.digest.HmacUtils;

import static com.github.badpop.jcoinbase.client.service.Headers.*;
import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

/** Manage all the security mechanism required by Coinbase to call protected resources */
@NoArgsConstructor
public class AuthenticationService {

  private static Void aVoid;

  /**
   * Get the headers required by the coinbase api to access protected resources like users or
   * transactions
   *
   * @param properties the JCoinbase properties presents int the JCoinbaseClient
   * @param httpMethod the http verb used to call the resource
   * @param httpPath the http path of the coinbase api resource
   * @param httpBody the http body used to call the resource
   * @return a String[] containing all the headers required by the coinbase api
   */
  public String[] getAuthenticationHeaders(
      final JCoinbaseProperties properties,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {
    return getAuthenticationHeaders(
        properties.getApiKey(),
        properties.getSecret(),
        properties.getApiVersion(),
        getCurrentTime(),
        httpMethod,
        httpPath,
        httpBody);
  }

  /**
   * Get the headers required by the coinbase api to access protected resources like users or
   * transactions
   *
   * @param apiKey a coinbase api key
   * @param secret a coinbase api secret
   * @param apiVersion a coinbase api version
   * @param timestamp the current system timestamp or, the coinbase api timestamp fetched using
   *     {@link com.github.badpop.jcoinbase.client.service.data.DataService} #getTime() method
   * @param httpMethod the http verb used to call the resource
   * @param httpPath the http path of the coinbase api resource
   * @param httpBody the http body used to call the resource
   * @return a String[] containing all the headers required by the coinbase api
   */
  public String[] getAuthenticationHeaders(
      final Option<String> apiKey,
      final Option<String> secret,
      final Option<String> apiVersion,
      final long timestamp,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {

    if (!isAllowed(apiKey, secret)) {
      val jcex =
          new JCoinbaseException(
              "You must specify an Api key and a secret to access this resource.");
      ErrorManagerService.manageOnError(jcex, jcex.getMessage(), jcex);
    }

    val message = timestamp + httpMethod + httpPath + ((httpBody == null) ? "" : httpBody);
    val signature = new HmacUtils(HMAC_SHA_256, secret.get().getBytes()).hmacHex(message);

    return new String[] {
      CB_ACCESS_SIGN.getValue(),
      signature,
      CB_ACCESS_TIMESTAMP.getValue(),
      String.valueOf(timestamp),
      CB_ACCESS_KEY.getValue(),
      apiKey.get(),
      CB_VERSION.getValue(),
      apiVersion.getOrElse(""),
      ACCEPT.getValue(),
      ACCEPT_VALUE.getValue()
    };
  }

  /** @return the system current time in seconds */
  private long getCurrentTime() {
    return System.currentTimeMillis() / 1000L;
  }

  /**
   * Really important method that check if the given client is properly build to access coinbase
   * protected resources.
   *
   * @param client The client we want to know if it can access the protected resources of the
   *     coinbase api
   * @return an {@link Either} containing a Left projection if not allowed, a Right projection
   *     otherwise
   */
  public Either<JCoinbaseException, Void> allow(final JCoinbaseClient client) {
    if (isAllowed(client.getProperties().getApiKey(), client.getProperties().getSecret())) {
      return Right(aVoid);
    } else {
      return Left(
          new JCoinbaseException(
              "You must specify an Api key and a secret to access this resource."));
    }
  }

  /**
   * @param apiKey the api key to check
   * @param secret the secret to check
   * @return true if allowed (non empty Options and non blank Options' values), false otherwise
   */
  private boolean isAllowed(final Option<String> apiKey, final Option<String> secret) {
    return (!apiKey.isEmpty() && !secret.isEmpty())
        && (!StringUtils.isBlank(apiKey.get()) && !StringUtils.isBlank(secret.get()));
  }
}
