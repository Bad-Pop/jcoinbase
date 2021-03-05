package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PriceDto {

  private final String base;
  private final String currency;
  private final BigDecimal amount;

  public Price toPrice(final PriceType priceType) {
    return Price.builder()
        .baseCurrency(base)
        .targetCurrency(currency)
        .amount(amount)
        .priceType(priceType)
        .build();
  }
}
