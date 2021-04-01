package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.CoinbaseError;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SingleErrorDto {
  private final String code;
  private final String message;
  private final String url;

  public CoinbaseError toCoinbaseError() {
    return CoinbaseError.builder().code(code).message(message).url(url).build();
  }
}
