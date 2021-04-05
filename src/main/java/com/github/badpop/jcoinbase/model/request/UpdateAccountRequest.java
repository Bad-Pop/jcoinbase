package com.github.badpop.jcoinbase.model.request;

import lombok.Builder;
import lombok.Value;

/**
 * Create an instance of this class to update the an account and set the fields value for the
 * fields you want to update. Leave other fields empty.
 */
@Value
@Builder
public class UpdateAccountRequest {
  String name;
}
