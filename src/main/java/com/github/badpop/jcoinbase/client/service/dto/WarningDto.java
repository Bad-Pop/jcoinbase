package com.github.badpop.jcoinbase.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class WarningDto {
  private final String id;
  private final String message;
  private final String url;
}
