package com.github.badpop.jcoinbase.model;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Optional;

import static io.vavr.API.List;

@Value
@Builder
public class Pagination {
  int limit;
  Order order;
  String endingBefore;
  String startingAfter;
  String previousEndingBefore;
  String nextStartingAfter;
  String previousUri;
  String nextUri;

  @Getter
  @AllArgsConstructor
  public enum Order {
    DESC("desc"),
    ASC("asc");

    private static final Seq<Order> values = List(values());
    private final String value;

    public static Optional<Order> fromStringAsJava(final String str) {
      return fromString(str).toJavaOptional();
    }

    public static Option<Order> fromString(final String str) {
      return values.find(order -> order.getValue().equalsIgnoreCase(str));
    }
  }
}
