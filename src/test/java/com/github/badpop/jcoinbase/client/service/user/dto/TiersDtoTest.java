package com.github.badpop.jcoinbase.client.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Tiers;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TiersDtoTest {

  @Test
  void should_map() {
    val completedDescription = "random";
    val dto = TiersDto.builder().completedDescription(completedDescription).build();

    val actual = dto.toTiers();

    assertThat(actual)
        .isEqualTo(Tiers.builder().completedDescription(completedDescription).build());
  }
}
