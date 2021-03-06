package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.vavr.API.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRatesDtoTest {

  @Test
  void should_return_ExchangeRates() {
    val currency = "BTC";
    val k1 = "EUR";
    val v1 = BigDecimal.valueOf(39000.42);
    val k2 = "USD";
    val v2 = BigDecimal.valueOf(48045.42);
    val dto = new ExchangeRatesDto(currency, Map(k1, v1, k2, v2));

    val actual = dto.toExchangeRates();

    assertThat(actual)
        .isEqualTo(ExchangeRates.builder().currency(currency).rates(Map(k1, v1, k2, v2)).build());
  }
}
