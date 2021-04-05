package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.Rewards;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RewardsDtoTest {

  @Test
  void should_map() {
    val dto = new RewardsDto("2", "2", "2 APY");
    val actual = dto.toRewards();
    assertThat(actual)
        .isInstanceOf(Rewards.class)
        .isEqualTo(Rewards.builder().apy("2").formattedApy("2").label("2 APY").build());
  }
}
