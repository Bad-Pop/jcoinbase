package com.github.badpop.jcoinbase.service.account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.service.account.dto.AccountDto;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.service.dto.PaginatedResponseDto;
import com.github.badpop.jcoinbase.service.http.HttpRequestSender;
import com.github.badpop.jcoinbase.service.utils.AuthenticationUtils;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;

public class CoinbaseAccountService {

  // TODO TEST
  protected Try<CallResult<Seq<CoinbaseError>, PaginatedResponse<Account>>> fetchAccountsList(
      final JCoinbaseClient client, final AuthenticationService authentication) {
    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl() + client.getProperties().getAccountsPath()))
            .headers(
                AuthenticationUtils.getHeaders(
                    authentication, client, "GET", client.getProperties().getAccountsPath(), ""))
            .build();

    return HttpRequestSender.paginatedSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<PaginatedResponseDto<AccountDto>>() {})
        .mapTry(
            callResult ->
                callResult.map(
                    page -> page.toPaginatedResponse(page.getData().map(AccountDto::toAccount))));
  }
}
