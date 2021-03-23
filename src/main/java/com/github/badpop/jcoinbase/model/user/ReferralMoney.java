package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

/** A class representing the Coinbase user's referral money model */
@Value
@Builder
public class ReferralMoney {
  String amount;
  String currency;
  String currencySymbol;
  String referralThreshold;
}
