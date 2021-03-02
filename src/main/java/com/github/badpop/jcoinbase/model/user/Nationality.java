package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Nationality {
  String code;
  String name;
}
