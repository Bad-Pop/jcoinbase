package com.github.badpop.jcoinbase.client.service.utils;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

  @Test
  void should_determine_that_string_is_not_blank() {
    val input = "input";
    val actual = StringUtils.isBlank(input);
    assertThat(actual).isFalse();
  }

  @Test
  void should_determine_that_string_is_blank_when_empty() {
    val input = "";
    val actual = StringUtils.isBlank(input);
    assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_whitespace() {
    val input = " ";
    val actual = StringUtils.isBlank(input);
    assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_null() {
    val actual = StringUtils.isBlank(null);
    assertThat(actual).isTrue();
  }
}
