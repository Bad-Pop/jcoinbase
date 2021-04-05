package com.github.badpop.jcoinbase.service.account;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.InvalidRequestException;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.exception.NoNextPageException;
import com.github.badpop.jcoinbase.exception.NoPreviousPageException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.Pagination;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountsPage;
import com.github.badpop.jcoinbase.model.request.UpdateAccountRequest;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.service.utils.StringUtils;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Option;
import static io.vavr.API.Try;

@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private static final String INVALID_ID_MESSAGE =
      "Please provide a non blank id to get an account by id";

  private final JCoinbaseClient client;
  private final CoinbaseAccountService service;
  private final AuthenticationService authentication;

  private AccountsPage toAccountsPage(final PaginatedResponse<Account> response) {
    return new AccountsPage(response.getPagination(), response.getData());
  }

  public CallResult<List<CoinbaseError>, AccountsPage> getAccountsPageAsJava() {
    return getAccountsPage().mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, AccountsPage> getAccountsPage() {
    return service
        .fetchAccountPageByUri(client, authentication, client.getProperties().getAccountsPath())
        .onSuccess(paginatedResponses -> log.info("Successfully get accounts page"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching accounts list",
                    throwable))
        .get()
        .map(this::toAccountsPage);
  }

  public CallResult<List<CoinbaseError>, AccountsPage> getNextAccountsPageAsJava(
      final Pagination pagination) {
    return getNextAccountsPage(pagination).mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, AccountsPage> getNextAccountsPage(
      final Pagination pagination) {

    val nextUri =
        Option(pagination.getNextUri())
            .onEmpty(
                () ->
                    ErrorManagerService.manageOnError(
                        new NoNextPageException("There is no next page available for your request"),
                        "There is no next page available for your request"))
            .get();

    return service
        .fetchAccountPageByUri(client, authentication, nextUri)
        .onSuccess(paginatedResponses -> log.info("Successfully fetch next accounts page"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching next accounts page",
                    throwable))
        .get()
        .map(this::toAccountsPage);
  }

  public CallResult<List<CoinbaseError>, AccountsPage> getPreviousAccountsPageAsJava(
      final Pagination pagination) {
    return getPreviousAccountsPage(pagination).mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, AccountsPage> getPreviousAccountsPage(
      final Pagination pagination) {

    val previousUri =
        Option(pagination.getPreviousUri())
            .onEmpty(
                () ->
                    ErrorManagerService.manageOnError(
                        new NoPreviousPageException(
                            "There is no previous page available for your request"),
                        "There is no previous page available for your request"))
            .get();

    return service
        .fetchAccountPageByUri(client, authentication, previousUri)
        .onSuccess(paginatedResponses -> log.info("Successfully fetch previous accounts page"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching previous accounts page",
                    throwable))
        .get()
        .map(this::toAccountsPage);
  }

  public CallResult<List<CoinbaseError>, Account> getAccountAsJava(final String id) {
    return getAccount(id).mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, Account> getAccount(final String id) {
    if (StringUtils.isBlank(id)) {
      ErrorManagerService.manageOnError(
          new InvalidRequestException(INVALID_ID_MESSAGE), INVALID_ID_MESSAGE);
    }

    val uri = client.getProperties().getAccountsPath() + "/" + id;

    return service
        .send(client, authentication, uri, "GET", "")
        .onSuccess(paginatedResponses -> log.info("Successfully fetch account by id"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching account by id",
                    throwable))
        .get();
  }

  public CallResult<List<CoinbaseError>, Account> updateAccountAsJava(
      final String id, final UpdateAccountRequest request) {
    return updateAccount(id, request).mapFailure(Seq::asJava);
  }

  public CallResult<Seq<CoinbaseError>, Account> updateAccount(
      final String id, final UpdateAccountRequest request) {
    Objects.requireNonNull(request, "request is null");
    if (StringUtils.isBlank(id)) {
      ErrorManagerService.manageOnError(
          new InvalidRequestException(INVALID_ID_MESSAGE), INVALID_ID_MESSAGE);
    }

    val uri = client.getProperties().getAccountsPath() + "/" + id;

    return Try(() -> client.getJsonSerDes().writeValueAsString(request))
        .flatMapTry(body -> service.send(client, authentication, uri, "PUT", body))
        .onSuccess(account -> log.info("Successfully update account"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while updating account with id",
                    throwable,
                    id))
        .get();
  }
}
