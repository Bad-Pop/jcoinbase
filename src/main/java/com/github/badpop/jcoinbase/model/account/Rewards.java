package com.github.badpop.jcoinbase.model.account;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rewards {
  String apy;
  String formattedApy;
  String label;
}
