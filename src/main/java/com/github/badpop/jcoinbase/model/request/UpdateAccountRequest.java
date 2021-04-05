package com.github.badpop.jcoinbase.model.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateAccountRequest {
  String name;
}
