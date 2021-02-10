package com.github.badpop.jcoinbase.client.dto.data;

import com.github.badpop.jcoinbase.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.badpop.jcoinbase.model.Price.PriceType.BUY;
import static org.assertj.core.api.Assertions.assertThat;

class PriceDtoTest {

  @Test
  void should_return_Price() {

    var dto = new PriceDto("BTC", "EUR", BigDecimal.valueOf(39875.47));

    var actual = dto.toPrice(BUY);

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
