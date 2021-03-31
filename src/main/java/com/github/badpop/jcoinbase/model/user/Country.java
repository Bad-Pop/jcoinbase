package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

/** Class representing the Coinbase country model */
@Value
@Builder
public class Country {
  String code;
  String name;
  boolean inEurope;
}
