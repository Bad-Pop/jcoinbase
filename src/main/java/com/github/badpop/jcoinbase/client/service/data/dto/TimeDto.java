package com.github.badpop.jcoinbase.client.service.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.data.Time;

import java.time.Instant;

public class TimeDto {

  private final Instant iso;
  private final long epoch;

  @JsonCreator
  public TimeDto(@JsonProperty("iso") Instant iso, @JsonProperty("epoch") long epoch) {
    this.iso = iso;
    this.epoch = epoch;
  }

  public Time toTime() {
    return Time.builder().iso(DateAndTimeUtils.fromInstant(iso).getOrNull()).epoch(epoch).build();
  }
}
