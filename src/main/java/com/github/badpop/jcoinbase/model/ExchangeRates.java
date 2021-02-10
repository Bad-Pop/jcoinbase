package com.github.badpop.jcoinbase.model;

import io.vavr.collection.Map;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRates {
  String currency;
  Map<String, BigDecimal> rates;

  public java.util.Map<String, BigDecimal> getRatesAsJavaMap(){
    return rates.toJavaMap();
  }
}
