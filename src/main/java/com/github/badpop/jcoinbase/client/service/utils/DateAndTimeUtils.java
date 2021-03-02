package com.github.badpop.jcoinbase.client.service.utils;

import io.vavr.control.Option;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.vavr.API.None;
import static io.vavr.API.Some;

public interface DateAndTimeUtils {

  static Option<LocalDateTime> fromInstant(final Instant instant) {
    if (instant != null) return Some(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    else return None();
  }
}
