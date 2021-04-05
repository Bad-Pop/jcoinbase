package com.github.badpop.jcoinbase.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.request.UpdateCurrentUserRequest;
import com.github.badpop.jcoinbase.model.user.Authorizations;
import com.github.badpop.jcoinbase.model.user.User;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This service allows you to request coinbase users and current user data. <strong>To properly use
 * this service, you must provide an API Key and an API secret when building a JCoinbaseClient
 * instance.</strong>
 */
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final JCoinbaseClient client;
  private final CoinbaseUserService service;
  private final AuthenticationService authentication;

  /**
   * Get the current user data based on the provided coinbase API Key.
   *
   * @return a {@link CallResult} containing a {@link User} object if it's ok, a List of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, User> getCurrentUserAsJava() {
    return getCurrentUser().mapFailure(Seq::asJava);
  }

  /**
   * Get the current user data based on the provided coinbase API Key.
   *
   * @return a {@link CallResult} containing a {@link User} object if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, User> getCurrentUser() {
    return service
        .fetchCurrentUser(client, authentication)
        .onSuccess(user -> log.info("Successfully fetch current user."))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching current user",
                    throwable))
        .get();
  }

  /**
   * Get current user’s authorization information including granted scopes
   *
   * @return a {@link CallResult} containing an {@link Authorizations} object if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, Authorizations> getAuthorizationsAsJava() {
    return getAuthorizations().mapFailure(Seq::asJava);
  }

  /**
   * Get current user’s authorization information including granted scopes
   *
   * @return a {@link CallResult} containing an {@link Authorizations} object if it's ok, a Seq of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, Authorizations> getAuthorizations() {
    return service
        .fetchAuthorizations(client, authentication)
        .onSuccess(user -> log.info("Successfully fetch current user."))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching current user",
                    throwable))
        .get();
  }

  /**
   * Get any user’s public information with their ID.
   *
   * @return a {@link CallResult} containing an {@link User} object if it's ok, a List of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, User> getUserByIdAsJava(final String userId) {
    return getUserById(userId).mapFailure(Seq::asJava);
  }

  /**
   * Get any user’s public information with their ID.
   *
   * @return a {@link CallResult} containing an {@link User} object if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, User> getUserById(final String userId) {
    return service
        .fetchUserById(client, authentication, userId)
        .onSuccess(user -> log.info("Successfully fetch user by id with id {}", userId))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching user by id with the given id {}",
                    throwable,
                    userId))
        .get();
  }

  /**
   * Modify current user and their preferences.
   *
   * @return a {@link CallResult} containing an {@link User} object if it's ok, a List of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, User> updateCurrentUserAsJava(
      final UpdateCurrentUserRequest request) {
    return updateCurrentUser(request).mapFailure(Seq::asJava);
  }

  /**
   * Modify current user and their preferences.
   *
   * @return a {@link CallResult} containing an {@link User} object if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, User> updateCurrentUser(
      final UpdateCurrentUserRequest request) {

    return service
        .updateCurrentUser(client, authentication, request)
        .onSuccess(user -> log.info("Successfully updated the current user"))
        .onFailure(
            JsonProcessingException.class,
            jsonProcessingExc ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(
                        "An error occurred while serializing request to json", jsonProcessingExc),
                    "An error occurred while serializing the request to json for the given request {}",
                    jsonProcessingExc,
                    request))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(
                        "An error occurred while updating the current user", throwable),
                    "An error occurred while updating the current user with this request {}",
                    throwable,
                    request))
        .get();
  }
}
