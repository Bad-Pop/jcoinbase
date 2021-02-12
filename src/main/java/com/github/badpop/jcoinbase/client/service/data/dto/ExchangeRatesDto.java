package com.github.badpop.jcoinbase.client.service.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import io.vavr.collection.Map;

import java.math.BigDecimal;

public class ExchangeRatesDto {

  private final String currency;
  private final Map<String, BigDecimal> rates;

  @JsonCreator
  public ExchangeRatesDto(
      @JsonProperty("currency") String currency,
      @JsonProperty("rates") Map<String, BigDecimal> rates) {
    this.currency = currency;
    this.rates = rates;
  }

  public ExchangeRates toExchangeRates() {
    return ExchangeRates.builder().currency(currency).rates(rates).build();
  }
}
