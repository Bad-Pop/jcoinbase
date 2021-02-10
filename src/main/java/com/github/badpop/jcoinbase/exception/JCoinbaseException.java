package com.github.badpop.jcoinbase.exception;

public class JCoinbaseException extends RuntimeException {

  public JCoinbaseException(String message) {
    super(message);
  }

  public JCoinbaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public JCoinbaseException(Throwable cause) {
    super(cause);
  }
}
