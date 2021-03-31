package com.github.badpop.jcoinbase.model.data;

import io.vavr.collection.Map;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/** Class representing the Coinbase exchange rates model */
@Value
@Builder
public class ExchangeRates {
  String currency;
  Map<String, BigDecimal> rates;

  /**
   * Return currency rates as a java Map instead of a Vavr Map
   *
   * @return a java Map containing all the rates
   */
  public java.util.Map<String, BigDecimal> getRatesAsJavaMap() {
    return rates.toJavaMap();
  }
}
