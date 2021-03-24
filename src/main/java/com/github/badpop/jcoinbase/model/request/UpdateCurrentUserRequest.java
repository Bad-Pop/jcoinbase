package com.github.badpop.jcoinbase.model.request;

import lombok.Builder;
import lombok.Value;

/**
 * Create an instance of this class to update the current user and set the fields value for the
 * fields you want to update. Leave other fields empty.
 */
@Value
@Builder
public class UpdateCurrentUserRequest {
  String name;
  String timeZone;
  String nativeCurrency;
}
