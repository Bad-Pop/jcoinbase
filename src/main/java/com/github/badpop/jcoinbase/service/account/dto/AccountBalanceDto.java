package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.AccountBalance;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class AccountBalanceDto {
  private final BigDecimal amount;
  private final String currency;

  public AccountBalance toAccountBalance() {
    return AccountBalance.builder().amount(amount).currency(currency).build();
  }
}
