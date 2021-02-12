package com.github.badpop.jcoinbase.client.service.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.data.Currency;

import java.math.BigDecimal;

public class CurrencyDto {

  private final String id;
  private final String name;
  private final BigDecimal minSize;

  @JsonCreator
  public CurrencyDto(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("min_size") BigDecimal minSize) {
    this.id = id;
    this.name = name;
    this.minSize = minSize;
  }

  public Currency toCurrency() {
    return Currency.builder().id(id).name(name).minSize(minSize).build();
  }
}
