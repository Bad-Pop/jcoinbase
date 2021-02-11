package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.badpop.jcoinbase.properties.JCoinbaseProperties;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.time.Duration;

import static java.net.http.HttpClient.Redirect.NEVER;
import static java.net.http.HttpClient.Redirect.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

class JCoinbaseClientTest {

  @Test
  void should_properly_build_JCoinbaseClient() {

    var actual = new JCoinbaseClient().build(null, null, 3, false);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);

    assertThat(actual.getClient()).isNotNull().isInstanceOf(HttpClient.class);
    assertThat(actual.getClient().connectTimeout()).contains(Duration.ofSeconds(3));
    assertThat(actual.getClient().followRedirects()).isEqualTo(NEVER);

    assertThat(actual.getJsonDeserializer()).isNotNull().isInstanceOf(ObjectMapper.class);
    assertThat(
            actual.getJsonDeserializer().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
        .isFalse();
    assertThat(actual.getJsonDeserializer().getRegisteredModuleIds())
        .isNotEmpty()
        .doesNotContainNull();
    assertThat(actual.getJsonDeserializer().getRegisteredModuleIds().size()).isEqualTo(2);

    assertThat(actual.getProperties()).isNotNull();
    assertThat(actual.getProperties()).isInstanceOf(JCoinbaseProperties.class);
    assertThat(actual.getProperties().getProperties()).isNotEmpty();
  }

  @Test
  void should_properly_build_JCoinbaseClient_with_followRedirect() {

    var actual = new JCoinbaseClient().build(null, null, 3, true);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);

    assertThat(actual.getClient()).isNotNull().isInstanceOf(HttpClient.class);
    assertThat(actual.getClient().connectTimeout()).contains(Duration.ofSeconds(3));
    assertThat(actual.getClient().followRedirects()).isEqualTo(NORMAL);

    assertThat(actual.getJsonDeserializer()).isNotNull().isInstanceOf(ObjectMapper.class);
    assertThat(
            actual.getJsonDeserializer().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
        .isFalse();
    assertThat(actual.getJsonDeserializer().getRegisteredModuleIds())
        .isNotEmpty()
        .doesNotContainNull();
    assertThat(actual.getJsonDeserializer().getRegisteredModuleIds().size()).isEqualTo(2);

    assertThat(actual.getProperties()).isNotNull();
    assertThat(actual.getProperties()).isInstanceOf(JCoinbaseProperties.class);
    assertThat(actual.getProperties().getProperties()).isNotEmpty();
  }
}
