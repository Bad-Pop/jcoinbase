package com.github.badpop.jcoinbase.client.service;

import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import io.vavr.collection.Seq;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.API.Option;

/** An utility class that allow JCoinbase to log warning returned by the coinbase api */
@Slf4j
@UtilityClass
public class WarningManagerService {

  /**
   * Log coinbase warnings if presents, do nothing otherwise
   *
   * @param data the {@link DataDto} computed from the coinbase response
   * @param <T> the type wanted initially by making the request
   */
  public <T> void alertIfCoinbaseHasReturnedWarnings(final DataDto<T> data) {
    Option(data.getWarnings())
        .map(Seq::asJava)
        .peek(warnings -> log.warn("Coinbase api return response with warning(s) : {}", warnings));
  }
}
