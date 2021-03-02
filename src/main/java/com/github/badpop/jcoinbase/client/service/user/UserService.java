package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.exception.ErrorManagerService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final JCoinbaseClient client;
  private final CoinbaseUserService service;
  private final AuthenticationService authentication;

  public User fetchCurrentUser() {
    return service
        .fetchCurrentUser(client, authentication)
        .onSuccess(user -> log.info("Successfully fetch current user."))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnFailure(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching current user",
                    throwable))
        .get();
  }
}
