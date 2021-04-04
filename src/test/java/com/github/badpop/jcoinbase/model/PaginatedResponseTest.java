package com.github.badpop.jcoinbase.model;

import com.github.badpop.jcoinbase.model.account.Account;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;

class PaginatedResponseTest {

  @Test
  void should_return_data_as_java_list() {
    val accounts = Seq(Account.builder().build());
    val paginatedResponse =
        PaginatedResponse.<Account>builder()
            .pagination(Pagination.builder().build())
            .data(accounts)
            .build();

    val actualJava = paginatedResponse.getDataAsJava();
    val actualVavr = paginatedResponse.getData();

    assertThat(actualJava)
        .isInstanceOf(List.class)
        .hasSize(1)
        .containsExactlyElementsOf(actualVavr)
        .containsExactlyElementsOf(accounts);
  }
}
