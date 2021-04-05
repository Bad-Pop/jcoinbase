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

/**
 * This service allows you to request coinbase accounts data. <strong>To properly use this service,
 * you must provide an API Key and an API secret when building a JCoinbaseClient instance.</strong>
 */
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private static final String INVALID_ID_MESSAGE =
      "Please provide a non blank id to get an account by id";

  private final JCoinbaseClient client;
  private final CoinbaseAccountService service;
  private final AuthenticationService authentication;

  /**
   * Get the first accounts page
   *
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<List<CoinbaseError>, AccountsPage> getAccountsPageAsJava() {
    return getAccountsPage().mapFailure(Seq::asJava);
  }

  /**
   * Get the first accounts page
   *
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a Seq of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
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

  /**
   * Get the next accounts page
   *
   * @param pagination a pagination object that will allow JCoinbase to request the next accounts
   *     page
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws NullPointerException if the pagination is null
   * @throws NoNextPageException if there is no next page
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<List<CoinbaseError>, AccountsPage> getNextAccountsPageAsJava(
      final Pagination pagination) {
    return getNextAccountsPage(pagination).mapFailure(Seq::asJava);
  }

  /**
   * Get the next accounts page
   *
   * @param pagination a pagination object that will allow JCoinbase to request the next accounts
   *     page
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a Seq of
   *     {@link CoinbaseError} otherwise.
   * @throws NullPointerException if the pagination is null
   * @throws NoNextPageException if there is no next page
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, AccountsPage> getNextAccountsPage(
      final Pagination pagination) {
    Objects.requireNonNull(pagination, "Pagination is null");
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

  /**
   * Get the previous accounts page
   *
   * @param pagination a pagination object that will allow JCoinbase to request the next accounts
   *     page
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws NullPointerException if the pagination is null
   * @throws NoPreviousPageException if there is no previous page
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<List<CoinbaseError>, AccountsPage> getPreviousAccountsPageAsJava(
      final Pagination pagination) {
    return getPreviousAccountsPage(pagination).mapFailure(Seq::asJava);
  }

  /**
   * Get the previous accounts page
   *
   * @param pagination a pagination object that will allow JCoinbase to request the next accounts
   *     page
   * @return a {@link CallResult} containing an {@link AccountsPage} object if it's ok, a Seq of
   *     {@link CoinbaseError} otherwise.
   * @throws NullPointerException if the pagination is null
   * @throws NoPreviousPageException if there is no previous page
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, AccountsPage> getPreviousAccountsPage(
      final Pagination pagination) {
    Objects.requireNonNull(pagination, "Pagination is null");
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

  /**
   * Get an account by its id
   *
   * @param id the account's id
   * @return a {@link CallResult} containing an {@link Account} object if it's ok, a List of {@link
   *     CoinbaseError} otherwise.
   * @throws InvalidRequestException if the given id is not valid
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<List<CoinbaseError>, Account> getAccountAsJava(final String id) {
    return getAccount(id).mapFailure(Seq::asJava);
  }

  /**
   * Get an account by its id
   *
   * @param id the account's id
   * @return a {@link CallResult} containing an {@link Account} object if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws InvalidRequestException if the given id is not valid
   * @throws JCoinbaseException on unknown errors
   */
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

  /**
   * Update an account by its id
   *
   * @param id the account's id
   * @param request a valid {@link UpdateAccountRequest} containing the changes you want to apply on
   *     this account
   * @return a {@link CallResult} containing the updated {@link Account} if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws NullPointerException if the given request is null
   * @throws InvalidRequestException if the given id is not valid
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<List<CoinbaseError>, Account> updateAccountAsJava(
      final String id, final UpdateAccountRequest request) {
    return updateAccount(id, request).mapFailure(Seq::asJava);
  }

  /**
   * Update an account by its id
   *
   * @param id the account's id
   * @param request a valid {@link UpdateAccountRequest} containing the changes you want to apply on
   *     this account
   * @return a {@link CallResult} containing the updated {@link Account} if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws NullPointerException if the given request is null
   * @throws InvalidRequestException if the given id is not valid
   * @throws JCoinbaseException on unknown errors
   */
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

  private AccountsPage toAccountsPage(final PaginatedResponse<Account> response) {
    return new AccountsPage(response.getPagination(), response.getData());
  }
}
