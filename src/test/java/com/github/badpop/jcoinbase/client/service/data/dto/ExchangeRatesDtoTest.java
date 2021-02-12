package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.vavr.API.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRatesDtoTest {

  @Test
  void should_return_ExchangeRates() {
    var dto =
        new ExchangeRatesDto(
            "BTC",
            Map(
                "EUR", BigDecimal.valueOf(39000.42),
                "USD", BigDecimal.valueOf(48045.42)));

    var actual = dto.toExchangeRates();

    assertThat(actual)
        .isEqualTo(
            ExchangeRates.builder()
                .currency("BTC")
                .rates(
                    Map(
                        "EUR", BigDecimal.valueOf(39000.42),
                        "USD", BigDecimal.valueOf(48045.42)))
                .build());
  }
}
