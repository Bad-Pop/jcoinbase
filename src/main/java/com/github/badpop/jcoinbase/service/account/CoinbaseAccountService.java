package com.github.badpop.jcoinbase.service.account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import com.github.badpop.jcoinbase.service.account.dto.AccountDto;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.dto.PaginatedResponseDto;
import com.github.badpop.jcoinbase.service.http.HttpRequestSender;
import com.github.badpop.jcoinbase.service.utils.AuthenticationUtils;
import com.github.badpop.jcoinbase.service.utils.StringUtils;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;

import static io.vavr.API.*;

public class CoinbaseAccountService {

  protected Try<CallResult<Seq<CoinbaseError>, PaginatedResponse<Account>>> fetchAccountPageByUri(
      final JCoinbaseClient client, final AuthenticationService authentication, final String uri) {

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(client.getProperties().getApiUrl() + uri))
            .headers(AuthenticationUtils.getHeaders(authentication, client, "GET", uri, ""))
            .build();

    return HttpRequestSender.paginatedSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<PaginatedResponseDto<AccountDto>>() {})
        .mapTry(
            call ->
                call.map(
                    page -> page.toPaginatedResponse(page.getData().map(AccountDto::toAccount))));
  }

  protected Try<CallResult<Seq<CoinbaseError>, Account>> send(
      JCoinbaseClient client,
      AuthenticationService authentication,
      final String uri,
      final String httpMethod,
      final String httpBody) {

    val request =
        defineHttpMethod(
            HttpRequest.newBuilder()
                .uri(URI.create(client.getProperties().getApiUrl() + uri))
                .headers(
                    AuthenticationUtils.getHeaders(
                        authentication, client, httpMethod.toUpperCase(), uri, httpBody)),
            httpMethod,
            httpBody);

    return HttpRequestSender.send(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<AccountDto>>() {})
        .mapTry(result -> result.map(data -> data.getData().toAccount()));
  }

  private HttpRequest defineHttpMethod(
      HttpRequest.Builder builder, final String httpMethod, final String httpBody) {
    val method =
        Match(httpMethod.toUpperCase())
            .of(Case($("GET"), "GET"), Case($("PUT"), "PUT"), Case($(), ""));

    if (StringUtils.isBlank(method)) {
      ErrorManagerService.manageOnError(
          new JCoinbaseException("Internal error : invalid http method"),
          "Internal error : invalid http method");
    }

    if (StringUtils.isBlank(httpBody)) {
      return builder.method(method, BodyPublishers.noBody()).build();
    }
    return builder.method(method, BodyPublishers.ofString(httpBody)).build();
  }
}
