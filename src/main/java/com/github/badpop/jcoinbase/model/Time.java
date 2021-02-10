package com.github.badpop.jcoinbase.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Time {
  LocalDateTime iso;
  long epoch;
}
