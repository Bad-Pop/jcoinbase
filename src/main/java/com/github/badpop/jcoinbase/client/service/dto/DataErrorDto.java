package com.github.badpop.jcoinbase.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DataErrorDto<T> {
  private final T error;
}
