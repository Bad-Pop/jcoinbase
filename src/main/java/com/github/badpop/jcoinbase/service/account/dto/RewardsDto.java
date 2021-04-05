package com.github.badpop.jcoinbase.service.account.dto;

import com.github.badpop.jcoinbase.model.account.Rewards;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RewardsDto {
  private final String apy;
  private final String formattedApy;
  private final String label;

  public Rewards toRewards() {
    return Rewards.builder().apy(apy).formattedApy(formattedApy).label(label).build();
  }
}
