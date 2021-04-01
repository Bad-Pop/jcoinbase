package com.github.badpop.jcoinbase.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.service.data.dto.TimeDto;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.dto.WarningDto;
import com.github.badpop.jcoinbase.service.user.dto.*;
import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import com.github.badpop.jcoinbase.testutils.JsonUtils;
import io.vavr.API;
import io.vavr.jackson.datatype.VavrModule;
import lombok.SneakyThrows;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpRequestSenderTest {

  private static final ObjectMapper JSON_SER_DES =
      new ObjectMapper()
          .findAndRegisterModules()
          .registerModule(new VavrModule())
          .registerModule(new JavaTimeModule())
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
          .configure(WRITE_DATES_AS_TIMESTAMPS, false);

  private static final HttpResponse<String> CURRENT_USER_HTTP_RESPONSE_OK =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 200;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/coinbaseUserService/current_user.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  private static final HttpResponse<String> CURRENT_USER_HTTP_RESPONSE_KO =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 400;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/errors.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  private static final HttpResponse<String> TIME_HTTP_RESPONSE_OK =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 200;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/coinbaseDataService/time.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  private static final HttpResponse<String> TIME_HTTP_RESPONSE_KO =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 400;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/error.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  private static final HttpRequest request =
      HttpRequest.newBuilder().uri(URI.create("http://domain.com/")).build();

  private static final BodyHandler<String> bodyHandler = BodyHandlers.ofString();

  @Mock private HttpClient httpClient;

  @Nested
  class DoQuery {
    @Test
    void should_return_try_containing_a_call_result_success()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(CURRENT_USER_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.send(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<UserDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isNotEmpty();
      assertThat(actual.isSuccess()).isTrue();
      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .isEqualTo(
              new DataDto<>(
                  UserDto.builder()
                      .id("ID")
                      .name("name")
                      .username("username")
                      .profileLocation("profileLocation")
                      .profileBio("profileBio")
                      .profileUrl("profileUrl")
                      .avatarUrl("avatarUrl")
                      .resource("user")
                      .resourcePath("/v2/user")
                      .email("email@email.com")
                      .legacyId("legacyId")
                      .timeZone("Paris")
                      .nativeCurrency("EUR")
                      .bitcoinUnit("BTC")
                      .state("France")
                      .country(
                          CountryDto.builder().name("France").code("FR").isInEurope(true).build())
                      .nationality(NationalityDto.builder().code("FR").name("France").build())
                      .regionSupportsFiatTransfers(true)
                      .regionSupportsCryptoToCryptoTransfers(true)
                      .createdAt(Instant.parse("2017-12-11T12:38:24Z"))
                      .supportsRewards(true)
                      .tiers(TiersDto.builder().completedDescription("Niveau 2").build())
                      .referralMoney(
                          ReferralMoneyDto.builder()
                              .amount("8.30")
                              .currency("EUR")
                              .currencySymbol("€")
                              .referralThreshold("83.00")
                              .build())
                      .hasBlockingBuyRestrictions(false)
                      .hasMadeAPurchase(true)
                      .hasBuyDepositPaymentMethods(true)
                      .hasUnverifiedBuyDepositPaymentMethods(false)
                      .needsKycRemediation(false)
                      .showInstantAchUx(false)
                      .userType("individual")
                      .build(),
                  API.Seq(
                      WarningDto.builder()
                          .id("missing_version")
                          .message("Please supply API version (YYYY-MM-DD) as CB-VERSION header")
                          .url("https://developers.coinbase.com/api#versioning")
                          .build())));
    }

    @Test
    void should_return_try_containing_a_call_result_failure()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(CURRENT_USER_HTTP_RESPONSE_KO);

      val actualTry =
          HttpRequestSender.send(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<UserDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isEmpty();
      assertThat(actual.isFailure()).isTrue();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_try_failure() throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(CURRENT_USER_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.send(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<TimeDto>>() {});

      VavrAssertions.assertThat(actualTry).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class DoSingleFailureQuery {
    @Test
    void should_return_try_containing_a_call_result_success()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(TIME_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.singleFailureSend(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<TimeDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isNotEmpty();
      assertThat(actual.isSuccess()).isTrue();
      Assertions.assertThat(actual.get()).isInstanceOf(DataDto.class);
      Assertions.assertThat(actual.get().getData())
          .usingRecursiveComparison()
          .isEqualTo(
              TimeDto.builder()
                  .iso(Instant.parse("2015-06-23T18:02:51Z"))
                  .epoch(1435082571)
                  .build());
      Assertions.assertThat(actual.get().getWarnings()).isNull();
    }

    @Test
    void should_return_try_containing_a_call_result_failure()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(TIME_HTTP_RESPONSE_KO);

      val actualTry =
          HttpRequestSender.singleFailureSend(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<TimeDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isEmpty();
      assertThat(actual.isFailure()).isTrue();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_return_try_failure() throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(TIME_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.singleFailureSend(
              httpClient, request, JSON_SER_DES, new TypeReference<DataDto<UserDto>>() {});

      VavrAssertions.assertThat(actualTry).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }
}
