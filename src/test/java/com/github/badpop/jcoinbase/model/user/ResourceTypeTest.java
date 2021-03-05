package com.github.badpop.jcoinbase.model.user;

import org.junit.jupiter.api.Test;

import static com.github.badpop.jcoinbase.model.user.ResourceType.USER;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class ResourceTypeTest {

  @Test
  void should_find_resource_from_string() {
    var resource = "user";
    var actual = ResourceType.fromString(resource);
    assertThat(actual).contains(USER);
  }

  @Test
  void should_not_find_resource_from_string() {
    var resource = "Lorem Ipsum Dolor Sit Amet";
    var actual = ResourceType.fromString(resource);
    assertThat(actual).isEmpty();
  }
}
