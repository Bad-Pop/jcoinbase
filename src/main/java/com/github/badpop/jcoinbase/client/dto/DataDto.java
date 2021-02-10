package com.github.badpop.jcoinbase.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DataDto<T> {

  T data;

  @JsonCreator
  public DataDto(@JsonProperty(value = "data") T data) {
    this.data = data;
  }
}
