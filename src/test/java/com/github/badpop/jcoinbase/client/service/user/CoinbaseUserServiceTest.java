package com.github.badpop.jcoinbase.client.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.request.UpdateCurrentUserRequest;
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

import static com.github.badpop.jcoinbase.model.ResourceType.USER;
import static com.github.badpop.jcoinbase.testutils.ReflectionUtils.setFieldValueForObject;
import static io.vavr.API.*;
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
    client = JCoinbaseClientFactory.build("loremIpsum", "dolorSitAmet", "2021-02-03", 3, false);
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    mockServer.reset();
    service = new CoinbaseUserService();
    setFieldValueForObject(client.getProperties(), "apiUrl", "http://localhost:" + port);
  }

  @Nested
  class FetchCurrentUser {
    @Test
    void should_return_CallResult_success() throws IOException {
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

  @Nested
  class FetchAuthorizations {
    @Test
    void should_return_CallResult_success() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user/auth"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(
                      JsonUtils.readResource("/json/coinbaseUserService/authorizations.json")));

      val actual = service.fetchAuthorizations(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);

      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .ignoringFields("referralMoney.currencySymbol")
          .isEqualTo(
              CallResult.success(
                  Authorizations.builder()
                      .method("api_key")
                      .scopes(Seq("wallet:accounts:read", "wallet:addresses:read"))
                      .build()));
    }

    @Test
    void should_return_CallResult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user/auth"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/errors.json")));

      val actual = service.fetchAuthorizations(client, client.getAuthService());

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isFailure()).isTrue();
      assertThat(actual.get().getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/user/auth"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.fetchCurrentUser(client, client.getAuthService());

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class FetchUserById {
    @Test
    void should_return_CallResult_success() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/users/id"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/coinbaseUserService/user_by_id.json")));

      val userId = "id";

      val actual = service.fetchUserById(client, client.getAuthService(), userId);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);

      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .ignoringFields("referralMoney.currencySymbol")
          .isEqualTo(
              CallResult.success(
                  User.builder()
                      .id("ID")
                      .name("name")
                      .username(None())
                      .profileLocation(None())
                      .profileBio(None())
                      .profileUrl(None())
                      .avatarUrl("avatarUrl")
                      .resourceType(USER)
                      .resourcePath("resourcePath")
                      .email("email")
                      .legacyId(null)
                      .timeZone(null)
                      .nativeCurrency(null)
                      .bitcoinUnit(null)
                      .state(None())
                      .country(null)
                      .nationality(null)
                      .regionSupportsFiatTransfers(false)
                      .regionSupportsCryptoToCryptoTransfers(false)
                      .createdAt(null)
                      .supportsRewards(false)
                      .tiers(null)
                      .referralMoney(null)
                      .hasBlockingBuyRestrictions(false)
                      .hasMadeAPurchase(false)
                      .hasBuyDepositPaymentMethods(false)
                      .hasUnverifiedBuyDepositPaymentMethods(false)
                      .needsKycRemediation(false)
                      .showInstantAchUx(false)
                      .userType(None())
                      .build()));
    }

    @Test
    void should_return_CallResult_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/users/id"))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/errors.json")));

      val userId = "id";

      val actual = service.fetchUserById(client, client.getAuthService(), userId);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isFailure()).isTrue();
      assertThat(actual.get().getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_failure() throws IOException {
      mockServer
          .when(request().withMethod("GET").withPath("/v2/users/id"))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val userId = "id";

      val actual = service.fetchUserById(client, client.getAuthService(), userId);

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }

  @Nested
  class UpdateCurrentUser {
    @Test
    void should_return_CallResult_success() throws IOException {

      val request =
          UpdateCurrentUserRequest.builder()
              .name("new name")
              .nativeCurrency("new native currency")
              .timeZone("new timezone")
              .build();

      mockServer
          .when(
              request()
                  .withMethod("PUT")
                  .withPath("/v2/user")
                  .withBody(client.getJsonSerDes().writeValueAsString(request)))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(
                      JsonUtils.readResource(
                          "/json/coinbaseUserService/update_current_user.json")));

      val actual = service.updateCurrentUser(client, client.getAuthService(), request);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);

      Assertions.assertThat(actual.get())
          .usingRecursiveComparison()
          .ignoringFields("referralMoney.currencySymbol")
          .isEqualTo(
              CallResult.success(
                  User.builder()
                      .id("id")
                      .name("new name")
                      .username(Some("username"))
                      .profileLocation(None())
                      .profileBio(None())
                      .profileUrl(None())
                      .avatarUrl("avatarUrl")
                      .resourceType(USER)
                      .resourcePath("resourcePath")
                      .email(null)
                      .legacyId(null)
                      .timeZone(null)
                      .nativeCurrency(null)
                      .bitcoinUnit(null)
                      .state(None())
                      .country(null)
                      .nationality(null)
                      .regionSupportsFiatTransfers(false)
                      .regionSupportsCryptoToCryptoTransfers(false)
                      .createdAt(null)
                      .supportsRewards(false)
                      .tiers(null)
                      .referralMoney(null)
                      .hasBlockingBuyRestrictions(false)
                      .hasMadeAPurchase(false)
                      .hasBuyDepositPaymentMethods(false)
                      .hasUnverifiedBuyDepositPaymentMethods(false)
                      .needsKycRemediation(false)
                      .showInstantAchUx(false)
                      .userType(None())
                      .build()));
    }

    @Test
    void should_return_CallResult_failure() throws IOException {

      val request =
          UpdateCurrentUserRequest.builder()
              .name("new name")
              .nativeCurrency("new native currency")
              .timeZone("new timezone")
              .build();

      mockServer
          .when(
              request()
                  .withMethod("PUT")
                  .withPath("/v2/user")
                  .withBody(client.getJsonSerDes().writeValueAsString(request)))
          .respond(
              response()
                  .withStatusCode(400)
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/errors.json")));

      val actual = service.updateCurrentUser(client, client.getAuthService(), request);

      assertThat(actual).isSuccess().containsInstanceOf(CallResult.class);
      Assertions.assertThat(actual.get().isFailure()).isTrue();
      assertThat(actual.get().getFailure()).containsExactly(CoinbaseErrorSampleProvider.getError());
    }

    @Test
    void should_return_failure() throws IOException {

      val request =
          UpdateCurrentUserRequest.builder()
              .name("new name")
              .nativeCurrency("new native currency")
              .timeZone("new timezone")
              .build();

      mockServer
          .when(
              request()
                  .withMethod("PUT")
                  .withPath("/v2/user")
                  .withBody(client.getJsonSerDes().writeValueAsString(request)))
          .respond(
              response()
                  .withHeader("Content-Type", "application/json")
                  .withBody(JsonUtils.readResource("/json/error.json")));

      val actual = service.updateCurrentUser(client, client.getAuthService(), request);

      assertThat(actual).isFailure().failBecauseOf(JsonProcessingException.class);
    }
  }
}
