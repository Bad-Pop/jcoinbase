package com.github.badpop.jcoinbase.model.account;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AccountBalance {
  BigDecimal amount;
  String currency;
}
