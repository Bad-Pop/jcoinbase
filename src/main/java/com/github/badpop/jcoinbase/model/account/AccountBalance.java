package com.github.badpop.jcoinbase.model.account;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/** Class representing the Coinbase account balance model */
@Value
@Builder
public class AccountBalance {
  BigDecimal amount;
  String currency;
}
