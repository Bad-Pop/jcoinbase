package com.github.badpop.jcoinbase.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbaseClientFactoryTest {

  @Test
  void should_return_new_instance_if_null() {
    var actual = JCoinbaseClientFactory.build(null, null);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    var computed = JCoinbaseClientFactory.build(null, null);

    var actual = JCoinbaseClientFactory.build(null, null);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }
}
