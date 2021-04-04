package com.github.badpop.jcoinbase.service.account;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.account.AccountsPage;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final JCoinbaseClient client;
  private final CoinbaseAccountService service;
  private final AuthenticationService authentication;

  public CallResult<java.util.List<CoinbaseError>, AccountsPage> getPaginatedAccountsListAsJava() {
    return getPaginatedAccountsList().mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, AccountsPage> getPaginatedAccountsList() {
    return service
        .fetchAccountsList(client, authentication)
        .onSuccess(paginatedResponses -> log.info("Successfully get accounts list"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching accounts list",
                    throwable))
        .get()
        .map(call -> new AccountsPage(call.getPagination(), call.getData()));
  }
}
