package com.github.badpop.jcoinbase.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Authorizations;
import lombok.val;
import org.junit.jupiter.api.Test;

import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationsDtoTest {

  @Test
  void should_map_to_authorizations() {
    val method = "api key";
    val scopeOne = "lorem:read";
    val scopeTwo = "ipsum:read";

    val dto = AuthorizationsDto.builder().method(method).scopes(Seq(scopeOne, scopeTwo)).build();

    val actual = dto.toAuthorizations();

    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(Authorizations.builder().method(method).scopes(Seq(scopeOne, scopeTwo)).build());
  }
}
