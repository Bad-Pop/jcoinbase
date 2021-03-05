package com.github.badpop.jcoinbase.client;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class JCoinbaseClientFactoryTest {

  @Test
  void should_not_return_thread_safe_singleton() {
    var computed =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, false);
    var actual =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, false);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseClient.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void should_return_new_instance_if_null() {
    var actual =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, false);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void should_return_same_instance_if_already_computed() {
    var computed =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, true);

    var actual =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, true);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }

  @Test
  void buildWithoutThreadSafeSingleton_should_not_return_thread_safe_singleton() {
    var computed =
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", 3);
    var actual =
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", 3);

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseClient.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }

  @Test
  void buildThreadSafeSingleton_should_return_new_instance_if_null() {
    var actual =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", 3);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);
  }

  @Test
  void buildThreadSafeSingleton_should_return_same_instance_if_already_computed() {
    var computed =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", 3);

    var actual =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
            "loremIpsum", "dolorSitAmet", 3);

    assertThat(actual).isSameAs(computed).isEqualTo(computed).hasSameHashCodeAs(computed);
  }

  @Test
  void should_set_timeout_to_3_if_less_than_1() {
    var defaultTimeout = 3L;
    var actualBuild =
        JCoinbaseClientFactory.build(null, null, 0, false)
            .getHttpClient();
    var actualBuildTS =
        JCoinbaseClientFactory.buildThreadSafeSingleton(
                null, null, 0)
            .getHttpClient();
    var actualBuildNTS =
        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
                null, null, 0)
            .getHttpClient();

    assertThat(actualBuild.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
    assertThat(actualBuildNTS.connectTimeout()).contains(Duration.of(defaultTimeout, SECONDS));
  }
}
