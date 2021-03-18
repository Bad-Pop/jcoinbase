package com.github.badpop.jcoinbase.client.properties;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbasePropertiesFactoryTest {

  @Test
  void should_not_return_thread_safe_singleton() {
    val computed = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton("loremIpsum", "dolorSitAmet", "2021-02-03");
    val actual = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton("loremIpsum", "dolorSitAmet", "2021-02-03");

    assertThat(actual)
            .isNotNull()
            .isInstanceOf(JCoinbaseProperties.class)
            .isNotEqualTo(computed)
            .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void should_return_new_instance_if_null() {
    val actual = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null, null);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseProperties.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    val computed = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null, null);

    val actual = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null, null);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }
}
