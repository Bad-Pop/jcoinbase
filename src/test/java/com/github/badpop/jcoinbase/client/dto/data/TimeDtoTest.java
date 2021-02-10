package com.github.badpop.jcoinbase.client.dto.data;

import com.github.badpop.jcoinbase.model.Time;
import com.github.badpop.jcoinbase.utils.DateAndTimeUtils;
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
