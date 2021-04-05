package com.github.badpop.jcoinbase.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Authorizations;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AuthorizationsDto {
  private final String method;
  private final Seq<String> scopes;

  public Authorizations toAuthorizations() {
    return Authorizations.builder().method(method).scopes(scopes).build();
  }
}
