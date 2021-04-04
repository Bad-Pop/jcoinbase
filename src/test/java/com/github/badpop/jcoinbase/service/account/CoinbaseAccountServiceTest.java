package com.github.badpop.jcoinbase.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.Pagination;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountBalance;
import com.github.badpop.jcoinbase.model.account.AccountCurrency;
import com.github.badpop.jcoinbase.service.utils.DateAndTimeUtils;
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

import static com.github.badpop.jcoinbase.model.Pagination.Order.DESC;
import static com.github.badpop.jcoinbase.model.ResourceType.ACCOUNT;
import static com.github.badpop.jcoinbase.model.account.AccountType.WALLET;
import static com.github.badpop.jcoinbase.testutils.ReflectionUtils.setFieldValueForObject;
import static io.vavr.API.Seq;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class CoinbaseAccountServiceTest {

  private static ClientAndServer mockServer;
  private static int port;
  private static JCoinbaseClient client;
  private CoinbaseAccountService service;

  @BeforeAll
  static void init() {
    port = PortFactory.findFreePort();
    mockServer = ClientAndServer.startClientAndServer(port);
    client = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    mockServer.reset();
    service = new CoinbaseAccountService();
    setFieldValueForObject(client.getProperties(), "apiUrl", "http://localhost:" + port);
  }

  @Nested
  class FetchAccountsList {
    @Test
    void should_return_CallResult_success() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/accounts"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(
                      JsonUtils.readResource("/json/coinbaseAccountService/account_list.json")));

      val actual = service.fetchAccountsList(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  PaginatedResponse.<Account>builder()
                      .pagination(
                          Pagination.builder()
                              .endingBefore(null)
                              .startingAfter(null)
                              .previousEndingBefore(null)
                              .nextStartingAfter("nsa")
                              .limit(25)
                              .order(DESC)
                              .previousUri(null)
                              .nextUri("/v2/accounts?starting_after=nsa")
                              .build())
                      .data(
                          Seq(
                              Account.builder()
                                  .id("id")
                                  .name("name")
                                  .primary(true)
                                  .type(WALLET)
                                  .creationDate(
                                      DateAndTimeUtils.fromInstant(
                                              Instant.parse("2021-01-20T14:34:30Z"))
                                          .getOrNull())
                                  .lastUpdateDate(
                                      DateAndTimeUtils.fromInstant(
                                              Instant.parse("2021-03-29T20:17:15Z"))
                                          .getOrNull())
                                  .resourceType(ACCOUNT)
                                  .resourcePath("resourcePath")
                                  .allowDeposits(true)
                                  .allowWithdrawals(true)
                                  .balance(
                                      AccountBalance.builder()
                                          .amount(BigDecimal.valueOf(0.0))
                                          .currency("currency")
                                          .build())
                                  .currency(
                                      AccountCurrency.builder()
                                          .code("code")
                                          .name("name")
                                          .color("color")
                                          .sortIndex(140)
                                          .exponent(8)
                                          .type("crypto")
                                          .addressRegex("addrRegex")
                                          .assetId("assetId")
                                          .slug("slug")
                                          .destinationTagName("destTagName")
                                          .destinationTagRegex("destTagRegex")
                                          .build())
                                  .build()))
                      .build()));
    }

    @Test
    void should_return_CallResult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/accounts"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/errors.json")));

      val actual = service.fetchAccountsList(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isFailure()).isTrue();
      assertThat(actual.get().getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/accounts"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchAccountsList(client, client.getAuthService());

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }
}
