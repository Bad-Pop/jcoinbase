package com.github.badpop.jcoinbase.model.data;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/** A class representing the Coinbase currency model */
@Value
@Builder
public class Currency {
  String id;
  String name;
  BigDecimal minSize;
}
