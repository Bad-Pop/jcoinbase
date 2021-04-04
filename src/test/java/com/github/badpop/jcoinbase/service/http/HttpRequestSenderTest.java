package com.github.badpop.jcoinbase.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.service.account.dto.AccountBalanceDto;
import com.github.badpop.jcoinbase.service.account.dto.AccountCurrencyDto;
import com.github.badpop.jcoinbase.service.account.dto.AccountDto;
import com.github.badpop.jcoinbase.service.data.dto.TimeDto;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.dto.PaginatedResponseDto;
import com.github.badpop.jcoinbase.service.dto.PaginationDto;
import com.github.badpop.jcoinbase.service.dto.WarningDto;
import com.github.badpop.jcoinbase.service.user.dto.*;
import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import io.vavr.API;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;

import static com.github.badpop.jcoinbase.testutils.HttpResponsesSamples.*;
import static com.github.badpop.jcoinbase.testutils.JsonSerDesSample.JSON_SER_DES;
import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpRequestSenderTest {

  private static final HttpRequest request =
      HttpRequest.newBuilder().uri(URI.create("http://domain.com/")).build();

  private static final BodyHandler<String> bodyHandler = BodyHandlers.ofString();

  @Mock private HttpClient httpClient;

  @Nested
  class Send {
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
  class paginatedSend {
    @Test
    void should_return_try_containing_a_call_result_success()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(ACCOUNTS_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.paginatedSend(
              httpClient,
              request,
              JSON_SER_DES,
              new TypeReference<PaginatedResponseDto<AccountDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isNotEmpty();
      assertThat(actual.isSuccess()).isTrue();

      val pagination =
          new PaginationDto(
              25, "desc", null, null, null, "nsa", null, "/v2/accounts?starting_after=nsa");
      val currency =
          new AccountCurrencyDto(
              "code",
              "name",
              "color",
              140,
              8,
              "crypto",
              "addrRegex",
              "assetId",
              "slug",
              "destTagName",
              "destTagRegex");

      val balance = new AccountBalanceDto(BigDecimal.valueOf(0.0), "currency");

      val account =
          new AccountDto(
              "id",
              "name",
              true,
              "wallet",
              currency,
              balance,
              Instant.parse("2021-01-20T14:34:30Z"),
              Instant.parse("2021-03-29T20:17:15Z"),
              "account",
              "resourcePath",
              true,
              true,
              null,
              null);

      val warning =
          WarningDto.builder()
              .id("invalid_version")
              .message("Please supply a valid API version in YYYY-MM-DD format")
              .url("https://developers.coinbase.com/api#versioning")
              .build();

      val dto = new PaginatedResponseDto<>(pagination, Seq(account), Seq(warning));

      Assertions.assertThat(actual.get()).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void should_return_try_containing_a_call_result_failure()
        throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(ACCOUNTS_HTTP_RESPONSE_KO);

      val actualTry =
          HttpRequestSender.paginatedSend(
              httpClient,
              request,
              JSON_SER_DES,
              new TypeReference<PaginatedResponseDto<AccountDto>>() {});

      VavrAssertions.assertThat(actualTry).isSuccess();

      val actual = actualTry.get();

      Assertions.assertThat(actual).isInstanceOf(CallResult.class).isNotNull().isEmpty();
      assertThat(actual.isFailure()).isTrue();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_try_failure() throws IOException, InterruptedException {
      when(httpClient.send(request, bodyHandler)).thenReturn(ACCOUNTS_HTTP_RESPONSE_OK);

      val actualTry =
          HttpRequestSender.paginatedSend(
              httpClient,
              request,
              JSON_SER_DES,
              new TypeReference<PaginatedResponseDto<TimeDto>>() {});

      VavrAssertions.assertThat(actualTry).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class SingleFailureSend {
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
