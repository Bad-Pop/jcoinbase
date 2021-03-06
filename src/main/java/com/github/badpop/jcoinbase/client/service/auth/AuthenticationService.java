package com.github.badpop.jcoinbase.client.service.auth;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.utils.StringUtils;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.codec.digest.HmacUtils;

import static com.github.badpop.jcoinbase.client.service.Headers.ACCEPT;
import static com.github.badpop.jcoinbase.client.service.Headers.ACCEPT_VALUE;
import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

@NoArgsConstructor
public class AuthenticationService {

  private static Void aVoid;

  public String[] getAuthenticationHeaders(
      final JCoinbaseProperties properties,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {
    return getAuthenticationHeaders(
        properties.getApiKey(),
        properties.getSecret(),
        getCurrentTime(),
        httpMethod,
        httpPath,
        httpBody);
  }

  public String[] getAuthenticationHeaders(
      final Option<String> apiKey,
      final Option<String> secret,
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
      "CB-ACCESS-SIGN",
      signature,
      "CB-ACCESS-TIMESTAMP",
      String.valueOf(timestamp),
      "CB-ACCESS-KEY",
      apiKey.get(),
      ACCEPT.getValue(),
      ACCEPT_VALUE.getValue()
    };
  }

  private long getCurrentTime() {
    return System.currentTimeMillis() / 1000L;
  }

  public Either<JCoinbaseException, Void> allow(final JCoinbaseClient client) {
    if (isAllowed(client.getProperties().getApiKey(), client.getProperties().getSecret())) {
      return Right(aVoid);
    } else {
      return Left(
          new JCoinbaseException(
              "You must specify an Api key and a secret to access this resource."));
    }
  }

  private boolean isAllowed(final Option<String> apiKey, final Option<String> secret) {
    return (!apiKey.isEmpty() && !secret.isEmpty())
        && (!StringUtils.isBlank(apiKey.get()) && !StringUtils.isBlank(secret.get()));
  }
}
