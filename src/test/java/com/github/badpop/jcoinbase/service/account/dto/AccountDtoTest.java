package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.ResourceType;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountType;
import com.github.badpop.jcoinbase.service.utils.DateAndTimeUtils;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AccountDtoTest {

  @Test
  void should_map_to_Account() {
    val currency =
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

    val balance = new AccountBalanceDto(BigDecimal.valueOf(10), "BTC");

    val dto =
        new AccountDto(
            "id",
            "name",
            false,
            "type",
            currency,
            balance,
            Instant.ofEpochSecond(192362328),
            Instant.ofEpochSecond(192362328),
            "resource",
            "resourcePath",
            true,
            true);

    val actual = dto.toAccount();

    assertThat(actual)
        .isInstanceOf(Account.class)
        .usingRecursiveComparison()
        .isEqualTo(
            Account.builder()
                .id("id")
                .name("name")
                .primary(false)
                .type(AccountType.UNKNOWN)
                .currency(currency.toAccountCurrency())
                .balance(balance.toAccountBalance())
                .creationDate(
                    DateAndTimeUtils.fromInstant(Instant.ofEpochSecond(192362328)).getOrNull())
                .lastUpdateDate(
                    DateAndTimeUtils.fromInstant(Instant.ofEpochSecond(192362328)).getOrNull())
                .resourceType(ResourceType.UNKNOWN)
                .resourcePath("resourcePath")
                .allowDeposits(true)
                .allowWithdrawals(true)
                .build());
  }
}
