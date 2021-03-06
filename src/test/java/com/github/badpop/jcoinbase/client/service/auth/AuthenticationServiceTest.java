package com.github.badpop.jcoinbase.client.service.auth;

import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbasePropertiesFactory;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.control.Option;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static io.vavr.API.Option;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AuthenticationServiceTest {

  private final AuthenticationService authenticationService = new AuthenticationService();

  @Nested
  class Headers {
    @Test
    void should_return_headers() {

      val apiKey = "loremIpsum";
      val secret = "dolorSitAmet";
      val timestamp = 1613126414L;
      val httpMethod = "GET";
      val httpPath = "/path";
      val httpBody = "{\"currency\" : \"BTC\"}";

      val actualWrapper =
          authenticationService.getAuthenticationHeaders(
              JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(apiKey, secret),
              httpMethod,
              httpPath,
              httpBody);

      val actual =
          authenticationService.getAuthenticationHeaders(
              Option(apiKey), Option(secret), timestamp, httpMethod, httpPath, httpBody);

      assertThat(actual)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .containsExactly(
              "CB-ACCESS-SIGN",
              "f537a3320e2d33e209e2de9165a3ffcb7bac95fea1c2dc7cc45a76fa20dd528c",
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
       POTENTIAL SOLUTION 1 : USE JAVA REFLECTION
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

      val apiKey = "loremIpsum";
      val secret = "dolorSitAmet";
      val timestamp = 1613126414L;
      val httpMethod = "GET";
      val httpPath = "/path";
      val httpBody = "";

      val actualWrapper =
          authenticationService.getAuthenticationHeaders(
              JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(apiKey, secret),
              httpMethod,
              httpPath,
              httpBody);

      val actual =
          authenticationService.getAuthenticationHeaders(
              Option(apiKey), Option(secret), timestamp, httpMethod, httpPath, httpBody);

      assertThat(actual)
          .isNotEmpty()
          .isInstanceOf(String[].class)
          .containsExactly(
              "CB-ACCESS-SIGN",
              "d80c4ebb1a2a8c85024b795b7a6bdbfec3e9198b429e7bf31a843affc2d0f9f2",
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

      val timestamp = 1613126414L;
      val httpMethod = "GET";
      val httpPath = "/path";
      val httpBody = "";

      val properties = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(null, null);
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
      val client =
          JCoinbaseClientFactory.build(
              "loremIpsumd", "dolorSitAmet", 3, false);
      val actual = authenticationService.allow(client);
      VavrAssertions.assertThat(actual).containsOnRight(VOID);
    }

    @Test
    void should_not_be_allowed() {
      val client =
          JCoinbaseClientFactory.build(null, null, 3, false);
      val actual = authenticationService.allow(client);
      VavrAssertions.assertThat(actual).containsLeftInstanceOf(JCoinbaseException.class);
    }
  }
}
