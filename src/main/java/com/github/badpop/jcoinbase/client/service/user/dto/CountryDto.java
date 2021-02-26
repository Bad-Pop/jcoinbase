package com.github.badpop.jcoinbase.client.service.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.model.user.Country;

public class CountryDto {

  private final String code;
  private final String name;

  @JsonCreator
  public CountryDto(@JsonProperty("code") String code, @JsonProperty("name") String name) {
    this.code = code;
    this.name = name;
  }

  public Country toCountry() {
    return Country.builder().code(code).name(name).build();
  }
}
