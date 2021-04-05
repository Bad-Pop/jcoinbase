package com.github.badpop.jcoinbase.service.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** An enum to manage and centralize all the http headers required by the Coinbase api */
@Getter
@AllArgsConstructor
public enum Headers {
  ACCEPT("Accept"),
  ACCEPT_VALUE("application/json"),
  CB_ACCESS_SIGN("CB-ACCESS-SIGN"),
  CB_ACCESS_TIMESTAMP("CB-ACCESS-TIMESTAMP"),
  CB_ACCESS_KEY("CB-ACCESS-KEY"),
  CB_VERSION("CB-VERSION");

  private final String value;
}
