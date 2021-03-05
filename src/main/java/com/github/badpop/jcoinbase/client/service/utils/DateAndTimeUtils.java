package com.github.badpop.jcoinbase.client.service.utils;

import io.vavr.control.Option;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.vavr.API.Option;

public interface DateAndTimeUtils {

  static Option<LocalDateTime> fromInstant(final Instant instant) {
    return Option(instant).map(value -> LocalDateTime.ofInstant(value, ZoneId.systemDefault()));
  }
}
