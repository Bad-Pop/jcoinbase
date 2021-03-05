package com.github.badpop.jcoinbase.client.service.dto;

import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataDto<T> {
  T data;
  Seq<WarningDto> warnings;
}
