package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReferralMoney {
  String amount;
  String currency;
  String currencySymbol;
  String referralThreshold;
}
