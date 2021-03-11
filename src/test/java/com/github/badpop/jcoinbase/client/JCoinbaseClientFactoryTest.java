package com.github.badpop.jcoinbase.client;

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
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", "2021-02-03", 3);
    val actual =
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", "2021-02-03", 3);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseClient.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void buildThreadSafeSingleton_should_return_new_instance_if_null() {
    val actual =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", "2021-02-03", 3);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void buildThreadSafeSingleton_should_return_same_instance_if_already_computed() {
    val computed =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", "2021-02-03", 3);

    val actual =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", "2021-02-03", 3);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }

  @Test
  void should_set_timeout_to_3_if_less_than_1() {
    val defaultTimeout = 3L;
    val actualBuild = JCoinbaseClientFactory.build(null, null, null, 0, false).getHttpClient();
    val actualBuildTS =
        JCoinbaseClientFactory.buildThreadSafeSingleton(null, null, null, 0).getHttpClient();
    val actualBuildNTS =
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(null, null, null, 0).getHttpClient();

    assertThat(actualBuild.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildNTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
  }
}
