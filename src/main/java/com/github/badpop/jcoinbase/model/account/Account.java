package com.github.badpop.jcoinbase.model.account;

import com.github.badpop.jcoinbase.model.ResourceType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/** Class representing the Coinbase account model */
@Value
@Builder
public class Account {
  String id;
  String name;
  boolean primary;
  AccountType type;
  AccountCurrency currency;
  AccountBalance balance;
  LocalDateTime creationDate;
  LocalDateTime lastUpdateDate;
  ResourceType resourceType;
  String resourcePath;
  boolean allowDeposits;
  boolean allowWithdrawals;
  String rewardsApy;
  Rewards rewards;
}
