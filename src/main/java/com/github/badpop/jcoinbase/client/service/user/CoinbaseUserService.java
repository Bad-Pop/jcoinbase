package com.github.badpop.jcoinbase.client.service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.WarningManagerService;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import com.github.badpop.jcoinbase.client.service.user.dto.AuthorizationsDto;
import com.github.badpop.jcoinbase.client.service.user.dto.UserDto;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.user.Authorizations;
import com.github.badpop.jcoinbase.model.user.User;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import static com.github.badpop.jcoinbase.client.service.JsonDeserializationService.deserialize;

@Slf4j
public class CoinbaseUserService {

  private static String[] getHeaders(
      final AuthenticationService authenticationService,
      final JCoinbaseClient client,
      final String httpMethod,
      final String httpPath,
      final String httpBody) {
    return authenticationService.getAuthenticationHeaders(
        client.getProperties(), httpMethod, httpPath, httpBody);
  }

  public Try<CallResult<Seq<CoinbaseError>, User>> fetchCurrentUser(
      final JCoinbaseClient client, final AuthenticationService authentication) {
    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl() + client.getProperties().getUserPath()))
            .headers(
                getHeaders(authentication, client, "GET", client.getProperties().getUserPath(), ""))
            .build();

    return Try.of(() -> client.getHttpClient().send(request, BodyHandlers.ofString()))
        .mapTry(
            response ->
                deserialize(
                    response, client.getJsonSerDes(), new TypeReference<DataDto<UserDto>>() {}))
        .mapTry(
            callResult ->
                callResult
                    .peek(WarningManagerService::alertIfCoinbaseHasReturnedWarnings)
                    .map(data -> data.getData().toUser()));
  }

  public Try<CallResult<Seq<CoinbaseError>, Authorizations>> fetchAuthorizations(
      final JCoinbaseClient client, final AuthenticationService authentication) {

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl()
                        + client.getProperties().getCurrentUserAuthorizationsPath()))
            .headers(
                getHeaders(
                    authentication,
                    client,
                    "GET",
                    client.getProperties().getCurrentUserAuthorizationsPath(),
                    ""))
            .build();

    return Try.of(() -> client.getHttpClient().send(request, BodyHandlers.ofString()))
        .mapTry(
            response ->
                deserialize(
                    response,
                    client.getJsonSerDes(),
                    new TypeReference<DataDto<AuthorizationsDto>>() {}))
        .mapTry(
            callResult ->
                callResult
                    .peek(WarningManagerService::alertIfCoinbaseHasReturnedWarnings)
                    .map(data -> data.getData().toAuthorizations()));
  }
}
