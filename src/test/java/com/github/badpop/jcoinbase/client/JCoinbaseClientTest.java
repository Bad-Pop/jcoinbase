package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.jcoinbase.client.service.data.DataService;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.user.UserService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.testutils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.time.Duration;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.net.http.HttpClient.Redirect.NEVER;
import static java.net.http.HttpClient.Redirect.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JCoinbaseClientTest {

  @Test
  void should_properly_build_JCoinbaseClient() {

    var actual = new JCoinbaseClient().build(null, null, 3, false, false);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);

    assertThat(actual.getHttpClient()).isNotNull().isInstanceOf(HttpClient.class);
    assertThat(actual.getHttpClient().connectTimeout()).contains(Duration.ofSeconds(3));
    assertThat(actual.getHttpClient().followRedirects()).isEqualTo(NEVER);

    assertThat(actual.getJsonSerDes()).isNotNull().isInstanceOf(ObjectMapper.class);
    assertThat(actual.getJsonSerDes().isEnabled(WRITE_DATES_AS_TIMESTAMPS)).isFalse();
    assertThat(actual.getJsonSerDes().getRegisteredModuleIds()).isNotEmpty().doesNotContainNull();
    assertThat(actual.getJsonSerDes().getRegisteredModuleIds().size()).isEqualTo(2);
    assertThat(actual.getJsonSerDes().getPropertyNamingStrategy()).isEqualTo(SNAKE_CASE);

    assertThat(actual.getProperties()).isNotNull();
    assertThat(actual.getProperties()).isInstanceOf(JCoinbaseProperties.class);
    assertThat(actual.getProperties().getProperties()).isNotEmpty();
  }

  @Test
  void should_properly_build_JCoinbaseClient_with_followRedirect() {

    var actual = new JCoinbaseClient().build(null, null, 3, true, false);

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseClient.class);

    assertThat(actual.getHttpClient()).isNotNull().isInstanceOf(HttpClient.class);
    assertThat(actual.getHttpClient().connectTimeout()).contains(Duration.ofSeconds(3));
    assertThat(actual.getHttpClient().followRedirects()).isEqualTo(NORMAL);

    assertThat(actual.getJsonSerDes()).isNotNull().isInstanceOf(ObjectMapper.class);
    assertThat(actual.getJsonSerDes().isEnabled(WRITE_DATES_AS_TIMESTAMPS)).isFalse();
    assertThat(actual.getJsonSerDes().getRegisteredModuleIds()).isNotEmpty().doesNotContainNull();
    assertThat(actual.getJsonSerDes().getRegisteredModuleIds().size()).isEqualTo(2);
    assertThat(actual.getJsonSerDes().getPropertyNamingStrategy()).isEqualTo(SNAKE_CASE);

    assertThat(actual.getProperties()).isNotNull();
    assertThat(actual.getProperties()).isInstanceOf(JCoinbaseProperties.class);
    assertThat(actual.getProperties().getProperties()).isNotEmpty();
  }

  @Test
  void should_return_DataService() throws NoSuchFieldException, IllegalAccessException {
    var client = new JCoinbaseClient().build(null, null, 3, false, false);

    var actual = client.data();

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(DataService.class)
        .isEqualTo(ReflectionUtils.getFieldForObject(client, "dataService"));
  }

  @Test
  void should_return_UserService() throws NoSuchFieldException, IllegalAccessException {
    var client = new JCoinbaseClient().build("loremIpsum", "dolorSitAmet", 3, false, false);

    var actual = client.user();

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(UserService.class)
        .isEqualTo(ReflectionUtils.getFieldForObject(client, "userService"));
  }

  @Test
  void should_not_return_UserService_if_not_allowed() {
    var client = new JCoinbaseClient().build(null, null, 3, false, false);

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(client::user)
        .withMessage(
            "com.github.badpop.jcoinbase.exception.JCoinbaseException: You must specify an Api key and a secret to access this resource.");
  }

  @Test
  void should_call_buildThreadSafeSingleton_on_build() {
    var computed =
        new JCoinbaseClient().build("loremIpsum", "dolorSitAmet", 3, false, true).getProperties();
    var actual =
        new JCoinbaseClient().build("loremIpsum", "dolorSitAmet", 3, false, true).getProperties();

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseProperties.class)
        .isEqualTo(computed)
        .hasSameHashCodeAs(computed);
  }

  @Test
  void should_call_buildWithoutThreadSafeSingleton_on_build() {
    var computed =
        new JCoinbaseClient().build("loremIpsum", "dolorSitAmet", 3, false, false).getProperties();
    var actual =
        new JCoinbaseClient().build("loremIpsum", "dolorSitAmet", 3, false, false).getProperties();

    assertThat(actual)
        .isNotNull()
        .isInstanceOf(JCoinbaseProperties.class)
        .isNotEqualTo(computed)
        .doesNotHaveSameHashCodeAs(computed);
  }
}
