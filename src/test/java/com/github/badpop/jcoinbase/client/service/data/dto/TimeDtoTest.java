package com.github.badpop.jcoinbase.client.service.data.dto;

import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.data.Time;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeDtoTest {

  @Test
  void should_return_time() {

    val now = Instant.now();
    val dto = new TimeDto(now, now.getEpochSecond());

    val actual = dto.toTime();

    assertThat(actual)
        .isEqualTo(
            Time.builder()
                .iso(DateAndTimeUtils.fromInstant(now).getOrNull())
                .epoch(now.getEpochSecond())
                .build());
  }
}
