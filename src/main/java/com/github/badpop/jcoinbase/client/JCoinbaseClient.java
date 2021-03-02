package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.client.service.data.CoinbaseDataService;
import com.github.badpop.jcoinbase.client.service.data.DataService;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbasePropertiesFactory;
import com.github.badpop.jcoinbase.client.service.user.CoinbaseUserService;
import com.github.badpop.jcoinbase.client.service.user.UserService;
import com.github.badpop.jcoinbase.exception.ErrorManagerService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.jackson.datatype.VavrModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.ZoneId;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.net.http.HttpClient.Redirect.NEVER;
import static java.net.http.HttpClient.Redirect.NORMAL;
import static java.time.temporal.ChronoUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class JCoinbaseClient {

  @Getter HttpClient httpClient;
  @Getter ObjectMapper jsonSerDes;
  @Getter JCoinbaseProperties properties;
  @Getter AuthenticationService authService;
  DataService dataService;
  UserService userService;

  public DataService data() {
    return dataService;
  }

  public UserService user() {
    var allowed = authService.allow(this);

    if (allowed.isLeft()) {
      manageNotAllowed(allowed.getLeft());
    }

    return userService;
  }

  protected JCoinbaseClient build(
      final String apiKey,
      final String secret,
      final long timeout,
      final boolean followRedirects,
      final ZoneId zoneId,
      final boolean threadSafe) {
    log.info("Start building new JCoinbase client !");

    buildJsonSerDes(zoneId);
    buildProperties(apiKey, secret, threadSafe);
    buildAuthService();
    buildDataService();
    buildUserService();
    buildHttpClient(followRedirects, timeout);

    log.info("JCoinbase client successfully built !");

    return this;
  }

  private void buildJsonSerDes(final ZoneId zoneId) {
    this.jsonSerDes =
        new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new VavrModule())
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setTimeZone(TimeZone.getTimeZone(zoneId))
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);
  }

  private void buildProperties(final String apiKey, final String secret, final boolean threadSafe) {
    this.properties =
        threadSafe
            ? JCoinbasePropertiesFactory.buildThreadSafeSingleton(apiKey, secret)
            : JCoinbasePropertiesFactory.buildWithoutThreadSafeSingleton(apiKey, secret);
  }

  private void buildAuthService() {
    this.authService = new AuthenticationService();
  }

  private void buildDataService() {
    this.dataService = new DataService(this, new CoinbaseDataService());
  }

  private void buildUserService() {
    this.userService = new UserService(this, new CoinbaseUserService(), authService);
  }

  private void buildHttpClient(final boolean followRedirects, final long timeout) {
    if (followRedirects) {
      this.httpClient =
          HttpClient.newBuilder()
              .connectTimeout(Duration.of(timeout, SECONDS))
              .followRedirects(NORMAL)
              .build();
    } else {
      this.httpClient =
          HttpClient.newBuilder()
              .connectTimeout(Duration.of(timeout, SECONDS))
              .followRedirects(NEVER)
              .build();
    }
  }

  private void manageNotAllowed(final Throwable throwable) {
    ErrorManagerService.manageOnFailure(
        new JCoinbaseException(throwable),
        "Unable to allow this request. Please make sure you correctly build your JCoinbaseClient with API KEY and SECRET",
        throwable);
  }
}
