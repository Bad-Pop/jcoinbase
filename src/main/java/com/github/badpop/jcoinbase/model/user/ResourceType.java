package com.github.badpop.jcoinbase.model.user;

import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Coinbase return some resources with a field named resource. This enum represents the possible
 * values of this field.
 */
@Getter
@AllArgsConstructor
public enum ResourceType {
  USER("user"),
  UNKNOWN("unknown");

  private static final Seq<ResourceType> values = API.List(values());
  private final String resource;

  /**
   * Retrieve a resource type from a string representation. This method return an Option containing
   * the founded ResourceType, otherwise the Option will be empty
   *
   * @param resource the string representation of the resource type as given by the coinbase api
   * @return an Option containing the ResourceType or empty
   */
  // TODO REFACTOR to allow retrieve as java Optional
  public static Option<ResourceType> fromString(final String resource) {
    return values.find(res -> res.getResource().equalsIgnoreCase(resource));
  }
}
