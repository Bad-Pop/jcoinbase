package com.github.badpop.jcoinbase.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CoinbaseError {
  String code;
  String message;
  String url;
}
