package com.github.badpop.jcoinbase.model;

import com.github.badpop.jcoinbase.model.Pagination.Order;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.badpop.jcoinbase.model.Pagination.Order.ASC;
import static com.github.badpop.jcoinbase.model.Pagination.Order.DESC;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class PaginationTest {

  @Nested
  class FromString {

    @Test
    void should_retrieve_order() {
      val desc = "DeSc";
      val asc = "aSc";

      val actualDesc = Order.fromString(desc);
      val actualAsc = Order.fromString(asc);

      assertThat(actualDesc).contains(DESC);
      assertThat(actualAsc).contains(ASC);
    }

    @Test
    void should_Not_retrieve_order() {
      val unknown = "lorem ipsum";
      val actual = Order.fromString(unknown);
      assertThat(actual).isEmpty();
    }
  }

  @Nested
  class FromStringAsJava {

    @Test
    void should_retrieve_order() {
      val desc = "DeSc";
      val asc = "aSc";

      val actualDesc = Order.fromStringAsJava(desc);
      val actualAsc = Order.fromStringAsJava(asc);

      Assertions.assertThat(actualDesc).contains(DESC);
      Assertions.assertThat(actualAsc).contains(ASC);
    }

    @Test
    void should_Not_retrieve_order() {
      val unknown = "lorem ipsum";
      val actual = Order.fromStringAsJava(unknown);
      Assertions.assertThat(actual).isEmpty();
    }
  }
}
