package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.CoinbaseError;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorDtoTest {

  @Test
  void should_map_CoinbaseError() {
    val id = "id";
    val message = "message";
    val url = "url";
    val dto = new ErrorDto(id, message, url);

    val actual = dto.toCoinbaseError();

    assertThat(actual)
        .isEqualTo(CoinbaseError.builder().code(id).message(message).url(url).build());
  }
}
