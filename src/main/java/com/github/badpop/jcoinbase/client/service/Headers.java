package com.github.badpop.jcoinbase.client.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Headers {
  ACCEPT("Accept"),
  ACCEPT_VALUE("application/json");

  private final String value;
}
