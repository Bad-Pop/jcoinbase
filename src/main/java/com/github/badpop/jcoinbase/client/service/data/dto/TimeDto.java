package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.data.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Builder
@AllArgsConstructor
public class TimeDto {

  private final Instant iso;
  private final long epoch;

  public Time toTime() {
    return Time.builder().iso(DateAndTimeUtils.fromInstant(iso).getOrNull()).epoch(epoch).build();
  }
}
