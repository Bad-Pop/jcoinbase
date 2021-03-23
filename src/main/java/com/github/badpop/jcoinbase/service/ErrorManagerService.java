package com.github.badpop.jcoinbase.service;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * An utility class to centralize error management when we need to log error and throw exceptions
 */
@Slf4j
@UtilityClass
public class ErrorManagerService {

  /**
   * Log a parameterized message and throws the given exception
   *
   * @param jcex a JCoinbase exception that must be thrown
   * @param message the message we want to log
   * @param logParams the message parameters to log
   */
  public void manageOnError(
      final JCoinbaseException jcex, final String message, final Object... logParams) {
    log.error(message, logParams);
    throw jcex;
  }

  /**
   * Log a parameterized message with a throwable and throws the given exception
   *
   * @param jcex a JCoinbase exception that must be thrown
   * @param message the message we want to log
   * @param throwable a throwable that we want to log
   * @param logParams the message parameters to log
   */
  public void manageOnError(
      final JCoinbaseException jcex,
      final String message,
      final Throwable throwable,
      final Object... logParams) {
    log.error(message, logParams, throwable);
    throw jcex;
  }
}
