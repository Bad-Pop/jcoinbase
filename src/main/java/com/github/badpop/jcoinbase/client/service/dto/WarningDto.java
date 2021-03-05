package com.github.badpop.jcoinbase.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class WarningDto {
  private final String id;
  private final String message;
  private final String url;
}
