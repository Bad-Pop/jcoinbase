package com.github.badpop.jcoinbase.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataErrorDto<T> {
  private final T error;
}
