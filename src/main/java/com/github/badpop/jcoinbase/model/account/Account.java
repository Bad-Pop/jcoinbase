package com.github.badpop.jcoinbase.model.account;

import com.github.badpop.jcoinbase.model.ResourceType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Account {
  String id;
  String name;
  boolean primary;
  AccountType type;
  String currency;
  AccountBalance balance;
  LocalDateTime creationDate;
  LocalDateTime lastUpdateDate;
  ResourceType resourceType;
  String resourcePath;
}
