package com.github.badpop.jcoinbase.model;

import lombok.Builder;
import lombok.Value;

/**
 * The CoinbaseError is a class that allows to return the errors returned by the coinbase api in a
 * non-breaking way.
 *
 * <p>For more information please take a look at <a
 * href="https://developers.coinbase.com/api/v2#errors">Coinbase API errors reference</a>
 */
@Value
@Builder
public class CoinbaseError {
  String code;
  String message;
  String url;
}
