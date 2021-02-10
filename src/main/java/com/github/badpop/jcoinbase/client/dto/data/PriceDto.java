package com.github.badpop.jcoinbase.client.dto.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.Price;
import com.github.badpop.jcoinbase.model.Price.PriceType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceDto {

  private final String base;
  private final String currency;
  private final BigDecimal amount;

  @JsonCreator
  public PriceDto(
      @JsonProperty(value = "base") String base,
      @JsonProperty(value = "currency") String currency,
      @JsonProperty(value = "amount") BigDecimal amount) {
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
