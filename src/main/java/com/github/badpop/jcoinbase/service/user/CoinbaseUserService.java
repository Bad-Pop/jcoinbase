package com.github.badpop.jcoinbase.service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.JCoinbaseProperties;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.request.UpdateCurrentUserRequest;
import com.github.badpop.jcoinbase.model.user.Authorizations;
import com.github.badpop.jcoinbase.model.user.User;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.http.HttpRequestSender;
import com.github.badpop.jcoinbase.service.user.dto.AuthorizationsDto;
import com.github.badpop.jcoinbase.service.user.dto.UserDto;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;

import static io.vavr.API.Tuple;

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

  protected Try<CallResult<Seq<CoinbaseError>, User>> fetchCurrentUser(
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

    return HttpRequestSender.send(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<UserDto>>() {})
        .mapTry(callResult -> callResult.map(data -> data.getData().toUser()));
  }

  protected Try<CallResult<Seq<CoinbaseError>, Authorizations>> fetchAuthorizations(
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

    return HttpRequestSender.send(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<AuthorizationsDto>>() {})
        .mapTry(callResult -> callResult.map(data -> data.getData().toAuthorizations()));
  }

  protected Try<CallResult<Seq<CoinbaseError>, User>> fetchUserById(
      final JCoinbaseClient client,
      final AuthenticationService authentication,
      final String userId) {

    val tupleUriPath = buildFetchUserByIdUriAndPath(client.getProperties(), userId);

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(tupleUriPath._1)
            .headers(getHeaders(authentication, client, "GET", tupleUriPath._2, ""))
            .build();

    return HttpRequestSender.send(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<UserDto>>() {})
        .mapTry(callResult -> callResult.map(data -> data.getData().toUser()));
  }

  protected Try<CallResult<Seq<CoinbaseError>, User>> updateCurrentUser(
      final JCoinbaseClient client,
      final AuthenticationService authentication,
      final UpdateCurrentUserRequest request) {

    return buildUpdateCurrentUserHttpRequest(client, authentication, request)
        .flatMapTry(
            httpRequest ->
                HttpRequestSender.send(
                    client.getHttpClient(),
                    httpRequest,
                    client.getJsonSerDes(),
                    new TypeReference<DataDto<UserDto>>() {}))
        .mapTry(callResult -> callResult.map(data -> data.getData().toUser()));
  }

  private Tuple2<URI, String> buildFetchUserByIdUriAndPath(
      final JCoinbaseProperties properties, final String userId) {
    return Tuple(
        URI.create(
            String.format("%s%s/%s", properties.getApiUrl(), properties.getUsersPath(), userId)),
        properties.getUsersPath() + "/" + userId);
  }

  private Try<HttpRequest> buildUpdateCurrentUserHttpRequest(
      final JCoinbaseClient client,
      final AuthenticationService authentication,
      final UpdateCurrentUserRequest request) {

    return Try.of(() -> client.getJsonSerDes().writeValueAsString(request))
        .mapTry(
            bodyAsString ->
                HttpRequest.newBuilder()
                    .PUT(BodyPublishers.ofString(bodyAsString))
                    .uri(
                        URI.create(
                            client.getProperties().getApiUrl()
                                + client.getProperties().getUserPath()))
                    .headers(
                        getHeaders(
                            authentication,
                            client,
                            "PUT",
                            client.getProperties().getUserPath(),
                            bodyAsString))
                    .build());
  }
}
