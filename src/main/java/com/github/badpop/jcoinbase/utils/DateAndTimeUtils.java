package com.github.badpop.jcoinbase.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface DateAndTimeUtils {

  static LocalDateTime fromInstant(final Instant instant) {
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
  }
}
