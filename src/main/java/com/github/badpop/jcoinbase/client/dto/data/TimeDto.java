package com.github.badpop.jcoinbase.client.dto.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.Time;
import com.github.badpop.jcoinbase.utils.DateAndTimeUtils;
import lombok.Getter;

import java.time.Instant;

@Getter
public class TimeDto {

  private final Instant iso;
  private final long epoch;

  @JsonCreator
  public TimeDto(
      @JsonProperty(value = "iso") Instant iso, @JsonProperty(value = "epoch") long epoch) {
    this.iso = iso;
    this.epoch = epoch;
  }

  public Time toTime() {
    return Time.builder().iso(DateAndTimeUtils.fromInstant(iso)).epoch(epoch).build();
  }
}
