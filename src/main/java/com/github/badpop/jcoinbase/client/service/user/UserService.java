package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.user.Authorizations;
import com.github.badpop.jcoinbase.model.user.User;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final JCoinbaseClient client;
  private final CoinbaseUserService service;
  private final AuthenticationService authentication;

  public CallResult<java.util.List<CoinbaseError>, User> getCurrentUserAsJava() {
    return getCurrentUser().mapFailure(Seq::asJava);
  }

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

  public CallResult<java.util.List<CoinbaseError>, Authorizations> getAuthorizationsAsJava() {
    return getAuthorizations().mapFailure(Seq::asJava);
  }

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

  public CallResult<java.util.List<CoinbaseError>, User> getUserByIdAsJava(final String userId) {
    return getUserById(userId).mapFailure(Seq::asJava);
  }

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
}
