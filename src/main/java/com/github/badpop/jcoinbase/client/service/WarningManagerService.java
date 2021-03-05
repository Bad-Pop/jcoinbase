package com.github.badpop.jcoinbase.client.service;

import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import io.vavr.collection.Seq;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.API.Option;

@Slf4j
@UtilityClass
public class WarningManagerService {

  public <DTO> void alertIfCoinbaseHasReturnedWarnings(final DataDto<DTO> data) {
    Option(data.getWarnings())
        .map(Seq::asJava)
        .peek(warnings -> log.warn("Coinbase api return response with warning(s) : {}", warnings));
  }
}
