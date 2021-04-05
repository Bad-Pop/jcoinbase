package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.AccountCurrency;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountCurrencyDto {
  private final String code;
  private final String name;
  private final String color;
  private final int sortIndex;
  private final int exponent;
  private final String type;
  private final String addressRegex;
  private final String assetId;
  private final String slug;
  private final String destinationTagName;
  private final String destinationTagRegex;

  public AccountCurrency toAccountCurrency() {
    return AccountCurrency.builder()
        .code(code)
        .name(name)
        .color(color)
        .sortIndex(sortIndex)
        .exponent(exponent)
        .type(type)
        .addressRegex(addressRegex)
        .assetId(assetId)
        .slug(slug)
        .destinationTagName(destinationTagName)
        .destinationTagRegex(destinationTagRegex)
        .build();
  }
}
