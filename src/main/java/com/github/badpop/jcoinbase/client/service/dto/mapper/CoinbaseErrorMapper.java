package com.github.badpop.jcoinbase.client.service.dto.mapper;

import com.github.badpop.jcoinbase.client.service.dto.ErrorDto;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import io.vavr.collection.Seq;

public interface CoinbaseErrorMapper {

  // TODO TEST
  static Seq<CoinbaseError> map(final Seq<ErrorDto> errorDtos) {
    return errorDtos.map(CoinbaseErrorMapper::map);
  }

  // TODO TEST
  static CoinbaseError map(final ErrorDto errorDto) {
    return CoinbaseError.builder()
        .code(errorDto.getId())
        .message(errorDto.getMessage())
        .url(errorDto.getUrl())
        .build();
  }
}
