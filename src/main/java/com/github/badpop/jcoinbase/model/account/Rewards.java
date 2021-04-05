package com.github.badpop.jcoinbase.model.account;

import lombok.Builder;
import lombok.Value;

/** Class representing the Coinbase account rewards model */
@Value
@Builder
public class Rewards {
  String apy;
  String formattedApy;
  String label;
}
