package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

/** Class representing the Coinbase user's tiers model */
@Value
@Builder
public class Tiers {
  String completedDescription;
}
