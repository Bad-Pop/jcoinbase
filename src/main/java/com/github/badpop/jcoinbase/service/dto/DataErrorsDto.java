package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.CoinbaseError;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataErrorsDto {
  private final Seq<ErrorDto> errors;

  public Seq<CoinbaseError> toCoinbaseErrors() {
    return errors.map(ErrorDto::toCoinbaseError);
  }
}
