package com.github.badpop.jcoinbase.client.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Time;
import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import com.github.badpop.jcoinbase.testutils.JsonUtils;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static com.github.badpop.jcoinbase.testutils.ReflectionUtils.setFieldValueForObject;
import static io.vavr.API.Map;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class CoinbaseDataServiceTest {

  private static ClientAndServer mockServer;
  private static int port;
  private static JCoinbaseClient client;
  private CoinbaseDataService service;

  @BeforeAll
  static void init() {
    port = PortFactory.findFreePort();
    mockServer = ClientAndServer.startClientAndServer(port);
    client = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    mockServer.reset();
    service = new CoinbaseDataService();
    setFieldValueForObject(client.getProperties(), "apiUrl", "http://localhost:" + port);
  }

  @Nested
  class FetchTime {
    @Test
    void should_return_time() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/time"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseDataService/time.json")));

      val actual = service.fetchTime(client);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isNotEmpty();
      Assertions.assertThat(actual.get().get())
          .isEqualTo(
              Time.builder()
                  .iso(
                      LocalDateTime.ofInstant(
                          Instant.ofEpochSecond(1435082571L), ZoneId.systemDefault()))
                  .epoch(1435082571L)
                  .build());
    }

    @Test
    void should_return_callresult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/time"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchTime(client);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isEmpty();
      assertThat(actual.get().getFailure())
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/time"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchTime(client);

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class FetchCurrencies {

    @Test
    void should_return_Currencies() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/currencies"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseDataService/currencies.json")));

      val actual = service.fetchCurrencies(client);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isSuccess()).isTrue();
      Assertions.assertThat(actual.get()).isNotEmpty();
      assertThat(actual.get().get())
          .containsExactly(
              Currency.builder()
                  .id("AED")
                  .name("United Arab Emirates Dirham")
                  .minSize(BigDecimal.valueOf(0.01))
                  .build());
    }

    @Test
    void should_return_callresult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/currencies"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchCurrencies(client);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isEmpty();
      assertThat(actual.get().getFailure())
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/currencies"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchCurrencies(client);

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class FetchExchangeRates {

    @Test
    void should_return_ExchangeRates() throws IOException {
      mockServer
          .when(
              request()
                  .withMethod("GET")
                  .withPath("/v2/exchange-rates")
                  .withQueryStringParameter("currency", "BTC"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(
                      JsonUtils.readResource("/json/coinbaseDataService/exchange_rates.json")));

      val actual = service.fetchExchangeRates(client, "BTC");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isNotEmpty();
      Assertions.assertThat(actual.get().get())
          .isEqualTo(
              ExchangeRates.builder()
                  .currency("BTC")
                  .rates(
                      Map(
                          "AAVE", BigDecimal.valueOf(86.09612201542411),
                          "AED", BigDecimal.valueOf(172914.68675109)))
                  .build());
    }

    @Test
    void should_return_callresult_failure() throws IOException {
      mockServer
          .when(
              request()
                  .withMethod("GET")
                  .withPath("/v2/exchange-rates")
                  .withQueryStringParameter("currency", "BTC"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchExchangeRates(client, "BTC");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isEmpty();
      assertThat(actual.get().getFailure())
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(
              request()
                  .withMethod("GET")
                  .withPath("/v2/exchange-rates")
                  .withQueryStringParameter("currency", "invalidCurrency"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchExchangeRates(client, "invalidCurrency");

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class FetchPrice {

    @Test
    void should_return_BUY_Price() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/prices/BTC-EUR/buy"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseDataService/price.json")));

      val actual = service.fetchPriceByType(client, BUY, "BTC", "EUR");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isNotEmpty();
      Assertions.assertThat(actual.get().get())
          .isEqualTo(
              Price.builder()
                  .baseCurrency("BTC")
                  .targetCurrency("EUR")
                  .amount(BigDecimal.valueOf(38800.72))
                  .priceType(BUY)
                  .build());
    }

    @Test
    void should_return_SELL_Price() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/prices/BTC-EUR/sell"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseDataService/price.json")));

      val actual = service.fetchPriceByType(client, SELL, "BTC", "EUR");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isNotEmpty();
      Assertions.assertThat(actual.get().get())
          .isEqualTo(
              Price.builder()
                  .baseCurrency("BTC")
                  .targetCurrency("EUR")
                  .amount(BigDecimal.valueOf(38800.72))
                  .priceType(SELL)
                  .build());
    }

    @Test
    void should_return_SPOT_Price() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/prices/BTC-EUR/spot"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseDataService/price.json")));

      val actual = service.fetchPriceByType(client, SPOT, "BTC", "EUR");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isNotEmpty();
      Assertions.assertThat(actual.get().get())
          .isEqualTo(
              Price.builder()
                  .baseCurrency("BTC")
                  .targetCurrency("EUR")
                  .amount(BigDecimal.valueOf(38800.72))
                  .priceType(SPOT)
                  .build());
    }

    @Test
    void should_return_callresult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/prices/BTC-EUR/spot"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchPriceByType(client, SPOT, "BTC", "EUR");

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get()).isEmpty();
      assertThat(actual.get().getFailure())
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/prices/BTC-invalidCurrency/spot"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchPriceByType(client, SPOT, "BTC", "invalidCurrency");

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }
}
