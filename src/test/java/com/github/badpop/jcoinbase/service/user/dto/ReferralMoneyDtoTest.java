package com.github.badpop.jcoinbase.service.user.dto;

import com.github.badpop.jcoinbase.model.user.ReferralMoney;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReferralMoneyDtoTest {

  @Test
  void should_map() {
    val amount = "8";
    val currency = "EUR";
    val currencySymbol = "€";
    val referralThreshold = "threshold";
    val dto =
        ReferralMoneyDto.builder()
            .amount(amount)
            .currency(currency)
            .currencySymbol(currencySymbol)
            .referralThreshold(referralThreshold)
            .build();

    val actual = dto.toReferralMoney();

    assertThat(actual)
        .isEqualTo(
            ReferralMoney.builder()
                .amount(amount)
                .currency(currency)
                .currencySymbol(currencySymbol)
                .referralThreshold(referralThreshold)
                .build());
  }
}
