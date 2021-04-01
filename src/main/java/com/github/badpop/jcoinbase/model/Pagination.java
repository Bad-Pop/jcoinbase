package com.github.badpop.jcoinbase.model;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.*;

import java.util.Optional;

import static io.vavr.API.List;

@Value
@Builder
public class Pagination {
  int limit;
  Order order;
  String endingBefore;
  String startingAfter;
  String previousUri;
  String nextUri;

  @Getter
  @AllArgsConstructor
  public static enum Order {
    DESC("desc"),
    ASC("asc");

    private static final Seq<Order> values = List(values());
    private final String order;

    public Optional<Order> fromStringAsJava(final String str) {
      return fromString(str).toJavaOptional();
    }

    public Option<Order> fromString(final String str) {
      return values.find(order -> order.getOrder().equalsIgnoreCase(str));
    }
  }
}
