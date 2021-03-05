package com.github.badpop.jcoinbase.client.service.properties;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbasePropertiesFactoryTest {

  @Test
  void should_not_return_thread_safe_singleton() {
    var computed = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton("loremIpsum", "dolorSitAmet");
    var actual = JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton("loremIpsum", "dolorSitAmet");

    assertThat(actual)
            .isNotNull()
            .isInstanceOf(JCoinbaseProperties.class)
            .isNotEqualTo(computed)
            .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void should_return_new_instance_if_null() {
    var actual = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseProperties.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    var computed = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null);

    var actual = JCoinbasePropertiesFactory.buildThreadSafeSingleton(null, null);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }
}
