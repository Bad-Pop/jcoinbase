package com.github.badpop.jcoinbase.client.dto.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.Currency;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CurrencyDto {

  private final String id;
  private final String name;
  private final BigDecimal minSize;

  @JsonCreator
  public CurrencyDto(
      @JsonProperty(value = "id") String id,
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "min_size") BigDecimal minSize) {
    this.id = id;
    this.name = name;
    this.minSize = minSize;
  }

  public Currency toCurrency() {
    return Currency.builder().id(id).name(name).minSize(minSize).build();
  }
}
