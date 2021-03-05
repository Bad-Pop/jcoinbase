package com.github.badpop.jcoinbase.testutils;

import com.github.badpop.jcoinbase.model.CoinbaseError;

public class CoinbaseErrorSampleProvider {

  public static CoinbaseError getSingleError() {
    return CoinbaseError.builder().code("error").message("error message").build();
  }

  public static CoinbaseError getSingleErrorWithUrl() {
    return CoinbaseError.builder().code("error").message("error message").url("url").build();
  }

  public static CoinbaseError getError() {
    return CoinbaseError.builder()
        .code("error")
        .message("error message")
        .url("http://localhost/doc")
        .build();
  }
}
