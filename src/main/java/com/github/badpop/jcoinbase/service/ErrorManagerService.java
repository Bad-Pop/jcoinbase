package com.github.badpop.jcoinbase.service;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ErrorManagerService {

  public void manageOnFailure(
          final JCoinbaseException jcex, final String message, final Object... logParams) {
    log.error(message, logParams);
    throw jcex;
  }

  public void manageOnFailure(
      final JCoinbaseException jcex,
      final String message,
      final Throwable throwable,
      final Object... logParams) {
    log.error(message, logParams, throwable);
    throw jcex;
  }
}
