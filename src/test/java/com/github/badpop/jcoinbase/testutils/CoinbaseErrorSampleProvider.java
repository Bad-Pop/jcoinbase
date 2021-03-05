package com.github.badpop.jcoinbase.testutils;

import com.github.badpop.jcoinbase.model.CoinbaseError;
import io.vavr.collection.Seq;

import static io.vavr.API.Seq;

public class CoinbaseErrorSampleProvider {

  public static CoinbaseError getSingleError() {
    return CoinbaseError.builder().code("error").message("error message").build();
  }

  public static CoinbaseError getError() {
    return CoinbaseError.builder()
        .code("error")
        .message("error message")
        .url("http://localhost/doc")
        .build();
  }

  public static Seq<CoinbaseError> getErrors() {
    return Seq(
        CoinbaseError.builder()
            .code("error")
            .message("error message")
            .url("http://localhost/doc")
            .build());
  }
}
