package com.github.badpop.jcoinbase.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Currency {
  String id;
  String name;
  BigDecimal minSize;
}
