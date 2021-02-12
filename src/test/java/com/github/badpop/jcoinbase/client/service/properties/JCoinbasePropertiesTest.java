package com.github.badpop.jcoinbase.client.service.properties;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class JCoinbasePropertiesTest {

  @Test
  void should_properly_build_JCoinbaseProperties() throws Exception {

    var inputStream = this.getClass().getClassLoader().getResourceAsStream("jcoinbase.properties");
    var expectedProperties = new Properties();
    expectedProperties.load(inputStream);

    var actual = new JCoinbaseProperties().build("key", "secret");

    assertThat(actual).isNotNull().isInstanceOf(JCoinbaseProperties.class);
    assertThat(actual.getProperties()).isNotEmpty();
    assertThat(actual.getProperties()).isEqualTo(expectedProperties);
    assertThat(actual.getApiKey()).contains("key");
    assertThat(actual.getSecret()).contains("secret");
    assertThat(actual.getApiUrl()).isEqualTo(expectedProperties.getProperty("coinbase.api.url"));
    assertThat(actual.getCurrentUserPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.currentUser"));
    assertThat(actual.getCurrentUserAuthorizationsPath())
        .isEqualTo(
            expectedProperties.getProperty("coinbase.api.path.resource.currentUserAuthorizations"));
    assertThat(actual.getUserPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.user"));
    assertThat(actual.getAccountPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.account"));
    assertThat(actual.getPaymentMethodPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.paymentMethods"));
    assertThat(actual.getCurrenciesPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.currencies"));
    assertThat(actual.getExchangeRatesPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.exchangeRates"));
    assertThat(actual.getPricesPath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.prices"));
    assertThat(actual.getTimePath())
        .isEqualTo(expectedProperties.getProperty("coinbase.api.path.resource.time"));
  }
}