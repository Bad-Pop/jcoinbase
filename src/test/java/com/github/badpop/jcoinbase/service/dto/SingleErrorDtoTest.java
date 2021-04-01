package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SingleErrorDtoTest {

  @Test
  @SuppressWarnings("java:S5845")
  void should_map_to_CoinbaseError() {
    val code = "error";
    val message = "error message";
    val dto = SingleErrorDto.builder().code(code).message(message).build();

    val actual = dto.toCoinbaseError();

    assertThat(actual).isEqualTo(CoinbaseErrorSampleProvider.getSingleError());
  }

  @Test
  @SuppressWarnings("java:S5845")
  void should_map_to_CoinbaseError_with_url() {
    val code = "error";
    val message = "error message";
    val url = "url";
    val dto = SingleErrorDto.builder().code(code).message(message).url(url).build();

    val actual = dto.toCoinbaseError();

    assertThat(actual).isEqualTo(CoinbaseErrorSampleProvider.getSingleErrorWithUrl());
  }
}
