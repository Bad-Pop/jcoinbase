package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.ResourceType;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountType;
import com.github.badpop.jcoinbase.service.utils.DateAndTimeUtils;
import lombok.AllArgsConstructor;

import java.time.Instant;

import static io.vavr.API.Option;

@AllArgsConstructor
public class AccountDto {
  private final String id;
  private final String name;
  private final boolean primary;
  private final String type;
  private final AccountCurrencyDto currency;
  private final AccountBalanceDto balance;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final String resource;
  private final String resourcePath;
  boolean allowDeposits;
  boolean allowWithdrawals;

  // TODO TEST
  public Account toAccount() {
    return Account.builder()
        .id(id)
        .name(name)
        .primary(primary)
        .type(AccountType.fromString(type).getOrElse(AccountType.UNKNOWN))
        .currency(Option(currency).map(AccountCurrencyDto::toAccountCurrency).getOrNull())
        .balance(Option(balance).map(AccountBalanceDto::toAccountBalance).getOrNull())
        .creationDate(DateAndTimeUtils.fromInstant(createdAt).getOrNull())
        .lastUpdateDate(DateAndTimeUtils.fromInstant(updatedAt).getOrNull())
        .resourceType(ResourceType.fromString(resource).getOrElse(ResourceType.UNKNOWN))
        .resourcePath(resourcePath)
        .allowDeposits(allowDeposits)
        .allowWithdrawals(allowWithdrawals)
        .build();
  }
}
