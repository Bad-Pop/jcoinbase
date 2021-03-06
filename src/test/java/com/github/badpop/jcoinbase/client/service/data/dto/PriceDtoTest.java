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
    val base = "BTC";
    val currency = "EUR";
    val amount = BigDecimal.valueOf(39875.47);
    val dto = new PriceDto(base, currency, amount);

    val actual = dto.toPrice(BUY);

    assertThat(actual)
        .isEqualTo(
            Price.builder()
                .baseCurrency(base)
                .targetCurrency(currency)
                .amount(amount)
                .priceType(BUY)
                .build());
  }
}
