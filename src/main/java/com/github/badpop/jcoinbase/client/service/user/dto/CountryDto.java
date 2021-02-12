package com.github.badpop.jcoinbase.client.service.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryDto {

  private final String code;
  private final String name;

  @JsonCreator
  public CountryDto(@JsonProperty("code") String code, @JsonProperty("name") String name) {
    this.code = code;
    this.name = name;
  }
}
