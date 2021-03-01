package com.github.badpop.jcoinbase.client.service.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface DateAndTimeUtils {

  // TODO MAKE IT NULL SAFE USING OPTION
  static LocalDateTime fromInstant(final Instant instant) {
    if (instant != null) return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    else return null;
  }
}
