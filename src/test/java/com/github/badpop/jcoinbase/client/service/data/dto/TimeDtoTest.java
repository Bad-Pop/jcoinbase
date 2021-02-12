package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.data.Time;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeDtoTest {

  @Test
  void should_return_time() {

    var now = Instant.now();
    var dto = new TimeDto(now, now.getEpochSecond());

    var actual = dto.toTime();

    assertThat(actual)
        .isEqualTo(
            Time.builder()
                .iso(DateAndTimeUtils.fromInstant(now))
                .epoch(now.getEpochSecond())
                .build());
  }
}
