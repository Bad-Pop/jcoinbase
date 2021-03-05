package com.github.badpop.jcoinbase.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.client.service.data.dto.TimeDto;
import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import com.github.badpop.jcoinbase.client.service.dto.WarningDto;
import com.github.badpop.jcoinbase.client.service.user.dto.*;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import com.github.badpop.jcoinbase.testutils.JsonUtils;
import io.vavr.jackson.datatype.VavrModule;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JsonDeserializationServiceTest {

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

  @Nested
  class Deserialize {
    @Test
    void should_properly_deserialize_and_return_success() throws JsonProcessingException {
      val typeRef = new TypeReference<DataDto<UserDto>>() {};

      val actual =
          JsonDeserializationService.deserialize(
              CURRENT_USER_HTTP_RESPONSE_OK, JSON_SER_DES, typeRef);

      assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isNotEmpty();
      assertThat(actual.isSuccess()).isTrue();
      assertThat(actual.get())
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
                  Seq(
                      WarningDto.builder()
                          .id("missing_version")
                          .message("Please supply API version (YYYY-MM-DD) as CB-VERSION header")
                          .url("https://developers.coinbase.com/api#versioning")
                          .build())));
    }

    @Test
    void should_properly_deserialize_and_return_failure() throws JsonProcessingException {
      val typeRef = new TypeReference<DataDto<UserDto>>() {};

      val actual =
          JsonDeserializationService.deserialize(
              CURRENT_USER_HTTP_RESPONSE_KO, JSON_SER_DES, typeRef);

      assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isEmpty();
      assertThat(actual.isFailure()).isTrue();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_throw_JsonProcessingException() {
      val typeRef = new TypeReference<DataDto<TimeDto>>() {};

      assertThatExceptionOfType(JsonProcessingException.class)
          .isThrownBy(
              () ->
                  JsonDeserializationService.deserialize(
                      CURRENT_USER_HTTP_RESPONSE_OK, JSON_SER_DES, typeRef));
    }
  }

  @Nested
  class SingleFailureDeserialize {

    @Test
    void should_properly_deserialize_and_return_success() throws JsonProcessingException {
      val typeRef = new TypeReference<DataDto<TimeDto>>() {};

      val actual =
          JsonDeserializationService.singleFailureDeserialize(
              TIME_HTTP_RESPONSE_OK, JSON_SER_DES, typeRef);

      assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isNotEmpty();
      assertThat(actual.isSuccess()).isTrue();
      assertThat(actual.get()).isInstanceOf(DataDto.class);
      assertThat(actual.get().getData())
          .usingRecursiveComparison()
          .isEqualTo(
              TimeDto.builder()
                  .iso(Instant.parse("2015-06-23T18:02:51Z"))
                  .epoch(1435082571)
                  .build());
      assertThat(actual.get().getWarnings()).isNull();
    }

    @Test
    void should_properly_deserialize_and_return_failure() throws JsonProcessingException {
      val typeRef = new TypeReference<DataDto<TimeDto>>() {};

      val actual =
          JsonDeserializationService.singleFailureDeserialize(
              TIME_HTTP_RESPONSE_KO, JSON_SER_DES, typeRef);

      assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isEmpty();
      assertThat(actual.isFailure()).isTrue();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());
    }

    @Test
    void should_throw_JsonProcessingException() {
      val typeRef = new TypeReference<DataDto<UserDto>>() {};

      assertThatExceptionOfType(JsonProcessingException.class)
          .isThrownBy(
              () ->
                  JsonDeserializationService.singleFailureDeserialize(
                      TIME_HTTP_RESPONSE_OK, JSON_SER_DES, typeRef));
    }
  }
}
