package com.github.badpop.jcoinbase.model.user;

import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceType {
  USER("user"),
  UNKNOWN("unknown");

  private static final Seq<ResourceType> values = API.List(values());
  private final String resource;

  public static Option<ResourceType> fromString(final String resource) {
    return values.find(res -> res.getResource().equalsIgnoreCase(resource));
  }
}
