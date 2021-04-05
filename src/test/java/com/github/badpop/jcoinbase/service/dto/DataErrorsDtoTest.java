package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.CoinbaseError;
import lombok.val;
import org.junit.jupiter.api.Test;

import static io.vavr.API.Seq;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class DataErrorsDtoTest {

  @Test
  void should_map_to_seq_of_CoinbaseError() {
    val id = "id";
    val message = "message";
    val url = "url";
    val dto = new DataErrorsDto(Seq(new ErrorDto(id, message, url)));

    val actual = dto.toCoinbaseErrors();

    assertThat(actual)
        .containsExactly(CoinbaseError.builder().code(id).message(message).url(url).build());
  }
}
