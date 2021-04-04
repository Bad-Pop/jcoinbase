package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.AccountCurrency;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountCurrencyDtoTest {

  @Test
  void should_map_to_AccountCurrency() {
    val dto =
        new AccountCurrencyDto(
            "BTC",
            "Bitcoin",
            "yellow",
            1,
            8,
            "crypto",
            "regex",
            "assetId",
            "bitcoin",
            "destTagName",
            "destTagRegex");

    val actual = dto.toAccountCurrency();

    assertThat(actual)
        .isInstanceOf(AccountCurrency.class)
        .usingRecursiveComparison()
        .isEqualTo(
            AccountCurrency.builder()
                .code("BTC")
                .name("Bitcoin")
                .color("yellow")
                .sortIndex(1)
                .exponent(8)
                .type("crypto")
                .addressRegex("regex")
                .assetId("assetId")
                .slug("bitcoin")
                .destinationTagName("destTagName")
                .destinationTagRegex("destTagRegex")
                .build());
  }
}
