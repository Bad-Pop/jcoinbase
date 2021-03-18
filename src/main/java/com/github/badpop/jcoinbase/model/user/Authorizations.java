package com.github.badpop.jcoinbase.model.user;

import io.vavr.collection.Seq;
import lombok.Builder;
import lombok.Value;

/** Coinbase current user's authorizations based on API key configuration */
@Value
@Builder
public class Authorizations {
  String method;
  Seq<String> scopes;

  /**
   * Return the Authorizations scopes as a java list instead of a vavr seq
   * @return a java list of scopes
   */
  public java.util.List<String> getScopesAsJava() {
    return scopes.asJava();
  }
}
