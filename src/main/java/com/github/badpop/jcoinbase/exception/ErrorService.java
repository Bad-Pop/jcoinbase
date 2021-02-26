package com.github.badpop.jcoinbase.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ErrorService {
  public void manageOnFailure(
      final JCoinbaseException jcex,
      final String message,
      final Throwable throwable,
      final Object... logParams) {
    log.error(message, logParams, throwable);
    throw jcex;
  }
}
