package com.github.badpop.jcoinbase.client.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.badpop.jcoinbase.model.user.Tiers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(
    value = {
      "upgrade_button_text",
      "header",
      "body"
    }) // TODO SEE WHAT MEANS THESE FIELDS IN COINBASE DOC
public class TiersDto {
  private final String completedDescription;

  public Tiers toTiers() {
    return Tiers.builder().completedDescription(completedDescription).build();
  }
}
