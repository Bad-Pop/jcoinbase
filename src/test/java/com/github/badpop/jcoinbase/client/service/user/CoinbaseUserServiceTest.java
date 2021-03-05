package com.github.badpop.jcoinbase.client.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.user.*;
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
import java.time.Instant;
import java.time.ZoneId;

import static com.github.badpop.jcoinbase.model.user.ResourceType.USER;
import static com.github.badpop.jcoinbase.testutils.ReflectionUtils.setFieldValueForObject;
import static io.vavr.API.Option;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class CoinbaseUserServiceTest {

  private static ClientAndServer mockServer;
  private static int port;
  private static JCoinbaseClient client;
  private CoinbaseUserService service;

  @BeforeAll
  static void init() {
    port = PortFactory.findFreePort();
    mockServer = ClientAndServer.startClientAndServer(port);
    client =
        JCoinbaseClientFactory.build(
            "loremIpsum", "dolorSitAmet", 3, false);
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    mockServer.reset();
    service = new CoinbaseUserService();
    setFieldValueForObject(client.getProperties(), "apiUrl", "http://localhost:" + port);
  }

  @Nested
  class FetchCurrentUserTest {

    @Test
    void should_return_CallResult_success_of_User() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseUserService/current_user.json")));

      val actual = service.fetchCurrentUser(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);

      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .ignoringFields("referralMoney.currencySymbol")
          .isEqualTo(
              CallResult.success(
                  User.builder()
                      .id("ID")
                      .name("name")
                      .username(Option("username"))
                      .profileLocation(Option("profileLocation"))
                      .profileBio(Option("profileBio"))
                      .profileUrl(Option("profileUrl"))
                      .avatarUrl("avatarUrl")
                      .resourceType(USER)
                      .resourcePath("/v2/user")
                      .email("email@email.com")
                      .legacyId("legacyId")
                      .timeZone("Paris")
                      .nativeCurrency("EUR")
                      .bitcoinUnit("BTC")
                      .state(Option("France"))
                      .country(Country.builder().name("France").code("FR").inEurope(true).build())
                      .nationality(Nationality.builder().code("FR").name("France").build())
                      .regionSupportsFiatTransfers(true)
                      .regionSupportsCryptoToCryptoTransfers(true)
                      .createdAt(
                          DateAndTimeUtils.fromInstant(Instant.parse("2017-12-11T12:38:24Z"))
                              .getOrNull())
                      .supportsRewards(true)
                      .tiers(Tiers.builder().completedDescription("Niveau 2").build())
                      .referralMoney(
                          ReferralMoney.builder()
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
                      .userType(Option("individual"))
                      .build()));
    }

    @Test
    void should_return_CallResult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/errors.json")));

      val actual = service.fetchCurrentUser(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isFailure()).isTrue();
      assertThat(actual.get().getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchCurrentUser(client, client.getAuthService());

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }
}
