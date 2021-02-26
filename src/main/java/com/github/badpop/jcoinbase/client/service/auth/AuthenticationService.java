package com.github.badpop.jcoinbase.client.service.auth;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.utils.StringUtils;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;

import static com.github.badpop.jcoinbase.exception.ErrorService.manageOnFailure;
import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

@NoArgsConstructor
public class AuthenticationService {

  private static Void VOID;

  // TODO TEST
  public String[] getAuthenticationHeaders(
      final JCoinbaseProperties properties,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {
    return getAuthenticationHeaders(
        properties.getApiKey().getOrNull(),
        properties.getSecret().getOrNull(),
        getCurrentTime(),
        httpMethod,
        httpPath,
        httpBody);
  }

  // TODO TEST
  public String[] getAuthenticationHeaders(
      final String apiKey,
      final String secret,
      final long timestamp,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {

    if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(secret)) {
      manageNotAllowed(
          new JCoinbaseException(
              "You must specify an Api key and a secret to access this resource."));
    }

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

  private long getCurrentTime() {
    return System.currentTimeMillis() / 1000L;
  }

  // TODO TEST
  public Either<JCoinbaseException, Void> allow(final JCoinbaseClient client) {
    if (isAllowed(client.getProperties().getApiKey(), client.getProperties().getSecret())) {
      return Right(VOID);
    } else {
      return Left(
          new JCoinbaseException(
              "You must specify an Api key and a secret to access this resource."));
    }
  }

  // TODO TEST
  public void manageNotAllowed(final JCoinbaseException e) {
    manageOnFailure(e, e.getMessage(), e);
  }

  private boolean isAllowed(final Option<String> apiKey, final Option<String> secret) {
    return (!apiKey.isEmpty() && !secret.isEmpty())
        && (!StringUtils.isBlank(apiKey.get()) && !StringUtils.isBlank(secret.get()));
  }
}
