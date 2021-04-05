package com.github.badpop.jcoinbase.model;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.badpop.jcoinbase.model.ResourceType.USER;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class ResourceTypeTest {

  @Test
  void should_find_resource_from_string_as_java() {
    val resource = "user";
    val actual = ResourceType.fromStringAsJava(resource);
    Assertions.assertThat(actual).contains(USER).isInstanceOf(Optional.class);
  }

  @Test
  void should_not_find_resource_from_string_as_java() {
    val resource = "Lorem Ipsum Dolor Sit Amet";
    val actual = ResourceType.fromStringAsJava(resource);
    Assertions.assertThat(actual).isEmpty().isInstanceOf(Optional.class);
  }

  @Test
  void should_find_resource_from_string() {
    val resource = "user";
    val actual = ResourceType.fromString(resource);
    assertThat(actual).contains(USER);
  }

  @Test
  void should_not_find_resource_from_string() {
    val resource = "Lorem Ipsum Dolor Sit Amet";
    val actual = ResourceType.fromString(resource);
    assertThat(actual).isEmpty();
  }
}
