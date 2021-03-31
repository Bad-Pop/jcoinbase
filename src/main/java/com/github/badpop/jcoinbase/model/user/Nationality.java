package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

/** Class representing the Coinbase nationality model */
@Value
@Builder
public class Nationality {
  String code;
  String name;
}
