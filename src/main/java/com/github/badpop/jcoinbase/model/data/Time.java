package com.github.badpop.jcoinbase.model.data;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/** A class representing the Coinbase time model */
@Value
@Builder
public class Time {
  LocalDateTime iso;
  long epoch;
}
