package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.model.data.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class CurrencyDto {

  private final String id;
  private final String name;
  private final BigDecimal minSize;

  public Currency toCurrency() {
    return Currency.builder().id(id).name(name).minSize(minSize).build();
  }
}
