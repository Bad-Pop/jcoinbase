package com.github.badpop.jcoinbase;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class JCoinbaseClientFactoryTest {

  @Test
  void should_not_return_thread_safe_singleton() {
    val computed =
        JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);
    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseClient.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void should_return_new_instance_if_null() {
    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    val computed =
        JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, true);

    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, true);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }

  @Test
  void buildWithoutThreadSafeSingleton_should_not_return_thread_safe_singleton() {
    val computed =
        JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);
    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseClient.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void buildThreadSafeSingleton_should_return_new_instance_if_null() {
    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, true);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void buildThreadSafeSingleton_should_return_same_instance_if_already_computed() {
    val computed =
        JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, true);

    val actual = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, true);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }

  @Test
  void should_set_timeout_to_3_if_less_than_1() {
    val defaultTimeout = 3L;
    val actualBuild = JCoinbaseClientFactory.build(null, null, null, 0, false).getHttpClient();
    val actualBuildTS = JCoinbaseClientFactory.build(null, null, null, 0, true).getHttpClient();
    val actualBuildNTS = JCoinbaseClientFactory.build(null, null, null, 0, false).getHttpClient();

    assertThat(actualBuild.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildNTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
  }
}
