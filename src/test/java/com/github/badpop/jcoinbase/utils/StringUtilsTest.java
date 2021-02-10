package com.github.badpop.jcoinbase.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

  @Test
  void should_determine_that_string_is_not_blank() {
    var input = "input";
    var actual = StringUtils.isBlank(input);
    assertThat(actual).isFalse();
  }

  @Test
  void should_determine_that_string_is_blank_when_empty() {
    var input = "";
    var actual = StringUtils.isBlank(input);
    assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_whitespace() {
    var input = " ";
    var actual = StringUtils.isBlank(input);
    assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_null() {
    var actual = StringUtils.isBlank(null);
    assertThat(actual).isTrue();
  }
}
