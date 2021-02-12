package com.github.badpop.jcoinbase.client.service.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;

import java.math.BigDecimal;

public class PriceDto {

  private final String base;
  private final String currency;
  private final BigDecimal amount;

  @JsonCreator
  public PriceDto(
      @JsonProperty("base") String base,
      @JsonProperty("currency") String currency,
      @JsonProperty("amount") BigDecimal amount) {
    this.base = base;
    this.currency = currency;
    this.amount = amount;
  }

  public Price toPrice(final PriceType priceType) {
    return Price.builder()
        .baseCurrency(base)
        .targetCurrency(currency)
        .amount(amount)
        .priceType(priceType)
        .build();
  }
}
