package com.github.badpop.jcoinbase.client.service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.DataDto;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.client.service.user.dto.UserDto;
import com.github.badpop.jcoinbase.model.user.User;
import io.vavr.control.Try;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class CoinbaseUserService {

  // TODO TEST
  public Try<User> fetchCurrentUser(
      final JCoinbaseClient client, final AuthenticationService authentication) {

    var requestHeaders =
        authentication.getAuthenticationHeaders(
            client.getProperties(), "GET", client.getProperties().getUserPath(), "");

    var request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl() + client.getProperties().getUserPath()))
            .headers(requestHeaders)
            .build();

    return Try.of(() -> client.getClient().send(request, BodyHandlers.ofString()))
        .mapTry(
            stringHttpResponse ->
                client
                    .getJsonSerDes()
                    .readValue(stringHttpResponse.body(), new TypeReference<DataDto<UserDto>>() {})
                    .getData()
                    .toUser());
  }
}
