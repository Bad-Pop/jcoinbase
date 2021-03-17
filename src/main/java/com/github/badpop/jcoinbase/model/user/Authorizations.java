package com.github.badpop.jcoinbase.model.user;

import io.vavr.collection.Seq;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Authorizations {
  String method;
  Seq<String> scopes;

  public java.util.List<String> getScopesAsJava() {
    return scopes.asJava();
  }
}
