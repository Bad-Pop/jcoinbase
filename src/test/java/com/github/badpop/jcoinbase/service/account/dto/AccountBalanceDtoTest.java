package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.AccountBalance;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountBalanceDtoTest {

  @Test
  void should_map_to_account_balance() {
    val balance = new AccountBalanceDto(BigDecimal.valueOf(10), "BTC");
    val actual = balance.toAccountBalance();
    assertThat(actual)
        .usingRecursiveComparison()
        .isInstanceOf(AccountBalance.class)
        .isEqualTo(AccountBalance.builder().amount(BigDecimal.valueOf(10)).currency("BTC").build());
  }
}
