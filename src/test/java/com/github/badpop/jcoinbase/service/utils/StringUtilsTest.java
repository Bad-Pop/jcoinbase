package com.github.badpop.jcoinbase.service.utils;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

  @Test
  void should_determine_that_string_is_not_blank() {
    val input = "input";
    val actual = StringUtils.isBlank(input);
    Assertions.assertThat(actual).isFalse();
  }

  @Test
  void should_determine_that_string_is_blank_when_empty() {
    val input = "";
    val actual = StringUtils.isBlank(input);
    Assertions.assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_whitespace() {
    val input = " ";
    val actual = StringUtils.isBlank(input);
    Assertions.assertThat(actual).isTrue();
  }

  @Test
  void should_determine_that_string_is_blank_when_null() {
    val actual = StringUtils.isBlank(null);
    Assertions.assertThat(actual).isTrue();
  }
}
