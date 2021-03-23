package com.github.badpop.jcoinbase.model.user;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationsTest {

  @Test
  void should_return_scopes_as_java() {
    val auths = Authorizations.builder().scopes(Seq("scope1:read", "scope2:write")).build();

    val actual = auths.getScopesAsJava();

    assertThat(actual)
        .isInstanceOf(List.class)
        .isNotEmpty()
        .hasSize(2)
        .containsExactlyElementsOf(auths.getScopes());
  }
}
