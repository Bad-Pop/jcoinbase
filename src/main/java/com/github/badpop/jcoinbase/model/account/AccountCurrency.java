package com.github.badpop.jcoinbase.model.account;

import lombok.Builder;
import lombok.Value;

/** Class representing the Coinbase account currency model */
@Value
@Builder
public class AccountCurrency {
  String code;
  String name;
  String color;
  int sortIndex;
  int exponent;
  String type;
  String addressRegex;
  String assetId;
  String destinationTagName;
  String destinationTagRegex;
  String slug;
}
