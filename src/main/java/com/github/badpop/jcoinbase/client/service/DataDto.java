package com.github.badpop.jcoinbase.client.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(value = {"warnings"}) // TODO IMPLEMENTS WARNINGS
public class DataDto<T> {
  T data;
}
