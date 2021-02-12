package com.github.badpop.jcoinbase.client.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Time;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static com.github.badpop.jcoinbase.testutils.ReflectionUtils.setFieldValueForObject;
import static io.vavr.API.List;
import static io.vavr.API.Map;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockitoExtension.class)
class CoinbaseDataServiceTest {

  private static ClientAndServer mockServer;
  private static int port;
  private static JCoinbaseClient client;
  private CoinbaseDataService service;

  @BeforeAll
  static void init() {
    port = PortFactory.findFreePort();
    mockServer = ClientAndServer.startClientAndServer(port);
    client = JCoinbaseClientFactory.build(null, null);
  }

  @BeforeEach
  void setUp() {
    mockServer.reset();
    service = new CoinbaseDataService();
    setFieldValueForObject(client.getProperties(), "apiUrl", "http://localhost:" + port);
  }

  @Test
  void getTime_should_return_time() {
    mockServer
        .when(request().withMethod("GET").withPath("/time"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                            "data": {
                              "iso": "2015-06-23T18:02:51Z",
                              "epoch": 1435082571
                            }
                          }
                          """));

    var actual = service.getTime(client);

    assertThat(actual).isSuccess().containsInstanceOf(Time.class);
    Assertions.assertThat(actual.get())
        .isEqualTo(
            Time.builder()
                .iso(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(1435082571L), ZoneId.systemDefault()))
                .epoch(1435082571L)
                .build());
  }

  @Test
  void getTime_should_return_failure() {
    mockServer
        .when(request().withMethod("GET").withPath("/time"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                            "error": {
                              "code": "error",
                              "message": "error message"
                            }
                          }
                          """));

    var actual = service.getTime(client);

    assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
  }

  @Test
  void getCurrencies_should_return_Currencies() {
    mockServer
        .when(request().withMethod("GET").withPath("/currencies"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                            "data": [
                              {
                                "id": "AED",
                                "name": "United Arab Emirates Dirham",
                                "min_size": "0.01"
                              }
                            ]
                          }
                          """));

    var actual = service.getCurrencies(client);
    var actualList = actual.get();

    assertThat(actual).isSuccess().containsInstanceOf(List(Currency.builder().build()).getClass());
    assertThat(actualList).hasSize(1);
    Assertions.assertThat(actual.get()).containsExactly(
        Currency.builder()
            .id("AED")
            .name("United Arab Emirates Dirham")
            .minSize(BigDecimal.valueOf(0.01))
            .build());
  }

  @Test
  void getCurrencies_should_return_failure() {
    mockServer
        .when(request().withMethod("GET").withPath("/currencies"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                            "error": [
                              {
                                "code": "error",
                                "message": "Error message"
                              }
                            ]
                          }
                          """));

    var actual = service.getCurrencies(client);

    assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
  }

  @Test
  void getExchangeRates_should_return_ExchangeRates(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/exchange-rates")
                .withQueryStringParameter("currency", "BTC"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                               "data": {
                                   "currency": "BTC",
                                   "rates": {
                                       "AAVE": "86.09612201542411",
                                       "AED": "172914.68675109"
                                   }
                               }
                           }
                          """));

    var actual = service.getExchangeRates(client, "BTC");

    assertThat(actual).isSuccess().containsInstanceOf(ExchangeRates.class);
    Assertions.assertThat(actual.get()).isEqualTo(
            ExchangeRates.builder()
                    .currency("BTC")
                    .rates(Map(
                      "AAVE", BigDecimal.valueOf(86.09612201542411),
                      "AED", BigDecimal.valueOf(172914.68675109)))
                    .build()
    );
  }

  @Test
  void getExchangeRates_should_return_failure(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/exchange-rates")
                .withQueryStringParameter("currency", "invalidCurrency"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                               "error": {
                                   "code": "error",
                                   "message": "error message"
                               }
                           }
                          """));

    var actual = service.getExchangeRates(client, "invalidCurrency");

    assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
  }

  @Test
  void getPriceByType_should_return_BUY_Price(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/prices/BTC-EUR/buy"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                              "data": {
                                  "base": "BTC",
                                  "currency": "EUR",
                                  "amount": "38800.72"
                              }
                          }
                          """));

    var actual = service.getPriceByType(client, BUY, "BTC", "EUR");

    assertThat(actual).isSuccess().containsInstanceOf(Price.class);
    Assertions.assertThat(actual.get()).isEqualTo(
            Price.builder()
                .baseCurrency("BTC")
                .targetCurrency("EUR")
                .amount(BigDecimal.valueOf(38800.72))
                .priceType(BUY)
                .build());
  }

  @Test
  void getPriceByType_should_return_SELL_Price(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/prices/BTC-EUR/sell"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                              "data": {
                                  "base": "BTC",
                                  "currency": "EUR",
                                  "amount": "38800.72"
                              }
                          }
                          """));

    var actual = service.getPriceByType(client, SELL, "BTC", "EUR");

    assertThat(actual).isSuccess().containsInstanceOf(Price.class);
    Assertions.assertThat(actual.get()).isEqualTo(
            Price.builder()
                    .baseCurrency("BTC")
                    .targetCurrency("EUR")
                    .amount(BigDecimal.valueOf(38800.72))
                    .priceType(SELL)
                    .build());
  }

  @Test
  void getPriceByType_should_return_SPOT_Price(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/prices/BTC-EUR/spot"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                              "data": {
                                  "base": "BTC",
                                  "currency": "EUR",
                                  "amount": "38800.72"
                              }
                          }
                          """));

    var actual = service.getPriceByType(client, SPOT, "BTC", "EUR");

    assertThat(actual).isSuccess().containsInstanceOf(Price.class);
    Assertions.assertThat(actual.get()).isEqualTo(
            Price.builder()
                    .baseCurrency("BTC")
                    .targetCurrency("EUR")
                    .amount(BigDecimal.valueOf(38800.72))
                    .priceType(SPOT)
                    .build());
  }

  @Test
  void getPriceByType_should_return_failure(){
    mockServer
        .when(
            request()
                .withMethod("GET")
                .withPath("/prices/BTC-invalidCurrency/spot"))
        .respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                          {
                              "error": {
                                  "code": "error",
                                  "message": "error message"
                              }
                          }
                          """));

    var actual = service.getPriceByType(client, SPOT, "BTC", "invalidCurrency");

    assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
  }
}
