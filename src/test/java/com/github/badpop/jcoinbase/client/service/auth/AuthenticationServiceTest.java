package com.github.badpop.jcoinbase.client.service.auth;

import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbasePropertiesFactory;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.control.Option;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.vavr.API.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AuthenticationServiceTest {

  private final AuthenticationService authenticationService = new AuthenticationService();

  @Nested
  class Headers {
    @Test
    void should_return_headers() {

      var apiKey = "loremIpsum";
      var secret = "dolorSitAmet";
      var timestamp = 1613126414L;
      var httpMethod = "GET";
      var httpPath = "/path";
      var httpBody = "{\"currency\" : \"BTC\"}";

      var actualWrapper =
          authenticationService.getAuthenticationHeaders(
              JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(apiKey, secret),
              httpMethod,
              httpPath,
              httpBody);

      var actual =
          authenticationService.getAuthenticationHeaders(
              Option(apiKey), Option(secret), timestamp, httpMethod, httpPath, httpBody);

      assertThat(actual)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .containsExactly(
              "CB-ACCESS-SIGN",
              "eceea3f528e7ceeb21aee8b9f5773b2c846d84f4d0c2379140cb7fb036d80a96",
              "CB-ACCESS-TIMESTAMP",
              "1613126414",
              "CB-ACCESS-KEY",
              "loremIpsum",
              "Accept",
              "application/json");

      /*
       We are not testing timestamp and signature
       due to incapacity to mock private long getCurrentTime().
       TODO find a better implementation and refactor
      */
      assertThat(actualWrapper)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .contains(
              "CB-ACCESS-SIGN",
              "CB-ACCESS-TIMESTAMP",
              "CB-ACCESS-KEY",
              "loremIpsum",
              "Accept",
              "application/json");
    }

    @Test
    void should_return_headers_even_if_body_is_empty() {

      var apiKey = "loremIpsum";
      var secret = "dolorSitAmet";
      var timestamp = 1613126414L;
      var httpMethod = "GET";
      var httpPath = "/path";
      var httpBody = "";

      var actualWrapper =
          authenticationService.getAuthenticationHeaders(
              JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(apiKey, secret),
              httpMethod,
              httpPath,
              httpBody);

      var actual =
          authenticationService.getAuthenticationHeaders(
              Option(apiKey), Option(secret), timestamp, httpMethod, httpPath, httpBody);

      assertThat(actual)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .containsExactly(
              "CB-ACCESS-SIGN",
              "5610cb5d35b50d65e3b66a82ca561f901508383b529abcb84e83a2abc5c8f0c2",
              "CB-ACCESS-TIMESTAMP",
              "1613126414",
              "CB-ACCESS-KEY",
              "loremIpsum",
              "Accept",
              "application/json");

      /*
       We are not testing timestamp and signature
       due to incapacity to mock private long getCurrentTime().
       TODO find a better implementation and refactor
      */
      assertThat(actualWrapper)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .contains(
              "CB-ACCESS-SIGN",
              "CB-ACCESS-TIMESTAMP",
              "CB-ACCESS-KEY",
              "loremIpsum",
              "Accept",
              "application/json");
    }

    @Test
    void should_not_return_headers_if_not_allowed() {

      var timestamp = 1613126414L;
      var httpMethod = "GET";
      var httpPath = "/path";
      var httpBody = "";

      var properties = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(null, null);
      Option<String> maybeParam = Option(null);

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(
              () ->
                  authenticationService.getAuthenticationHeaders(
                      properties, httpMethod, httpPath, httpBody))
          .withMessage("You must specify an Api key and a secret to access this resource.");

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(
              () ->
                  authenticationService.getAuthenticationHeaders(
                      maybeParam, maybeParam, timestamp, httpMethod, httpPath, httpBody))
          .withMessage("You must specify an Api key and a secret to access this resource.");
    }
  }

  @Nested
  class Allow {

    private Void VOID;

    @Test
    void should_be_allowed() {
      var client = JCoinbaseClientFactory.build("loremIpsumd", "dolorSitAmet", 3, false, false);
      var actual = authenticationService.allow(client);
      VavrAssertions.assertThat(actual).containsOnRight(VOID);
    }

    @Test
    void should_not_be_allowed() {
      var client = JCoinbaseClientFactory.build(null, null, 3, false, false);
      var actual = authenticationService.allow(client);
      VavrAssertions.assertThat(actual).containsLeftInstanceOf(JCoinbaseException.class);
    }
  }
}
