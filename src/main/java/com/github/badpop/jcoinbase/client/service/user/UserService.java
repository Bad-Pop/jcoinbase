package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class UserService {

  private final JCoinbaseClient client;
  private final CoinbaseUserService service;
}
