package com.github.badpop.jcoinbase.client.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DataDto<T> {

  T data;

  @JsonCreator
  public DataDto(@JsonProperty("data") T data) {
    this.data = data;
  }
}
