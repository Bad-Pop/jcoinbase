package com.github.badpop.jcoinbase.client.dto.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.ExchangeRates;
import io.vavr.collection.Map;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ExchangeRatesDto {

  private final String currency;
  private final Map<String, BigDecimal> rates;

  @JsonCreator
  public ExchangeRatesDto(
      @JsonProperty(value = "currency") String currency,
      @JsonProperty(value = "rates") Map<String, BigDecimal> rates) {
    this.currency = currency;
    this.rates = rates;
  }

  public ExchangeRates toExchangeRates() {
    return ExchangeRates.builder().currency(currency).rates(rates).build();
  }
}
