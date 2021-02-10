package com.github.badpop.jcoinbase.properties;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbasePropertiesFactoryTest {

  @Test
  void should_return_new_instance_if_null() {
    var actual = JCoinbasePropertiesFactory.getProperties(null, null);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseProperties.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    var computed = JCoinbasePropertiesFactory.getProperties(null, null);

    var actual = JCoinbasePropertiesFactory.getProperties(null, null);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }
}
