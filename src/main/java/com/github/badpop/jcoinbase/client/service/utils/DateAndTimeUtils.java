package com.github.badpop.jcoinbase.client.service.utils;

import io.vavr.control.Option;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.vavr.API.Option;

/**
 * Utility interface giving access to methods working on date and times like Instants,
 * LocalDateTime, ...
 */
public interface DateAndTimeUtils {

  /**
   * This method transform a Java {@link Instant} to a Java {@link LocalDateTime} using the system
   * default time zone and wrap it into a Vavr {@link Option}
   *
   * @param instant The instant to transform to a LocalDateTime
   * @return an Option containing the LocalDateTime if it's ok, an empty Option otherwise
   */
  static Option<LocalDateTime> fromInstant(final Instant instant) {
    return Option(instant).map(value -> LocalDateTime.ofInstant(value, ZoneId.systemDefault()));
  }
}
