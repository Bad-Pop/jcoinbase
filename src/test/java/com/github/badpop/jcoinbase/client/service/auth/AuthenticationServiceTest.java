package com.github.badpop.jcoinbase.client.service.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

  private final AuthenticationService authenticationService = new AuthenticationService();

  @Test
  void should_return_headers() {

    var apiKey = "loremIpsum";
    var secret = "dolorSitAmet";
    var timestamp = 1613126414L;
    var httpMethod = "GET";
    var httpPath = "/path";
    var httpBody = "{\"currency\" : \"BTC\"}";

    var actual =
            authenticationService.getAuthenticationHeaders(
                    apiKey, secret, timestamp, httpMethod, httpPath, httpBody);

    assertThat(actual).isNotEmpty().isInstanceOf(String[].class);
    assertThat(actual)
            .containsExactly(
                    "CB-ACCESS-SIGN",
                    "f537a3320e2d33e209e2de9165a3ffcb7bac95fea1c2dc7cc45a76fa20dd528c",
                    "CB-ACCESS-TIMESTAMP",
                    "1613126414",
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

    var actual =
        authenticationService.getAuthenticationHeaders(
            apiKey, secret, timestamp, httpMethod, httpPath, httpBody);

    assertThat(actual).isNotEmpty().isInstanceOf(String[].class);
    assertThat(actual)
        .containsExactly(
            "CB-ACCESS-SIGN",
            "d80c4ebb1a2a8c85024b795b7a6bdbfec3e9198b429e7bf31a843affc2d0f9f2",
            "CB-ACCESS-TIMESTAMP",
            "1613126414",
            "CB-ACCESS-KEY",
            "loremIpsum",
            "Accept",
            "application/json");
  }
}
