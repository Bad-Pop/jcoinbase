package com.github.badpop.jcoinbase.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
public class WarningDto {
  private final String id;
  private final String message;
  private final String url;
}
