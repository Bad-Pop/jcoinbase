package com.github.badpop.jcoinbase.service;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ErrorManagerServiceTest {

  @Test
  void should_throw_exception() {
    val ex = new JCoinbaseException("error");
    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> ErrorManagerService.manageOnError(ex, "message"));
  }

  @Test
  void should_throw_exception_with_throwable() {
    val throwable = new Throwable();
    val ex = new JCoinbaseException("error", throwable);
    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> ErrorManagerService.manageOnError(ex, "message", throwable));
  }
}
