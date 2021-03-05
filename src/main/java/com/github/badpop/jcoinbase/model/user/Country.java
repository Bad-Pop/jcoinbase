package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Country {
  String code;
  String name;
  boolean inEurope;
}
