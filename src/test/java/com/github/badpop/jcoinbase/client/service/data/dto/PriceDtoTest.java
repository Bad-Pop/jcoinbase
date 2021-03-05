package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.model.data.Price;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.BUY;
import static org.assertj.core.api.Assertions.assertThat;

class PriceDtoTest {

  @Test
  void should_return_Price() {

    val dto = new PriceDto("BTC", "EUR", BigDecimal.valueOf(39875.47));

    val actual = dto.toPrice(BUY);

    assertThat(actual)
        .isEqualTo(
            Price.builder()
                .baseCurrency("BTC")
                .targetCurrency("EUR")
                .amount(BigDecimal.valueOf(39875.47))
                .priceType(BUY)
                .build());
  }
}
