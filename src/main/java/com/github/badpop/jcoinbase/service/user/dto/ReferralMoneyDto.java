package com.github.badpop.jcoinbase.service.user.dto;

import com.github.badpop.jcoinbase.model.user.ReferralMoney;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ReferralMoneyDto {
  private final String amount;
  private final String currency;
  private final String currencySymbol;
  private final String referralThreshold;

  public ReferralMoney toReferralMoney() {
    return ReferralMoney.builder()
        .amount(amount)
        .currency(currency)
        .currencySymbol(currencySymbol)
        .referralThreshold(referralThreshold)
        .build();
  }
}
