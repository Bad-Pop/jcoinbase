package com.github.badpop.jcoinbase.client.properties;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbasePropertiesFactoryTest {

  @Test
  void should_not_return_thread_safe_singleton() {
    val computed =
        JCoinbasePropertiesFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", false);
    val actual =
        JCoinbasePropertiesFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", false);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseProperties.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void should_return_new_instance_if_null() {
    val actual = JCoinbasePropertiesFactory.build(null, null, null, true);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseProperties.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    val computed = JCoinbasePropertiesFactory.build(null, null, null, true);

    val actual = JCoinbasePropertiesFactory.build(null, null, null, true);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }
}
