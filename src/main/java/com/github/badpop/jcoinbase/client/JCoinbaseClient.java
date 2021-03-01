package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.client.service.data.CoinbaseDataService;
import com.github.badpop.jcoinbase.client.service.data.DataService;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbasePropertiesFactory;
import com.github.badpop.jcoinbase.client.service.user.CoinbaseUserService;
import com.github.badpop.jcoinbase.client.service.user.UserService;
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
import static com.github.badpop.jcoinbase.exception.ErrorService.*;
import static java.net.http.HttpClient.Redirect.NEVER;
import static java.net.http.HttpClient.Redirect.NORMAL;
import static java.time.temporal.ChronoUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class JCoinbaseClient {

  @Getter HttpClient client;
  @Getter ObjectMapper jsonSerDes;
  @Getter JCoinbaseProperties properties;
  AuthenticationService authService;
  DataService dataService;
  UserService userService;

  //TODO TEST
  public DataService data() {
    return dataService;
  }

  //TODO TEST
  public UserService user() {
    var allowed = authService.allow(this);

    if (allowed.isLeft()) {
      manageNotAllowed(allowed.getLeft());
    }

    return userService;
  }

  protected JCoinbaseClient build(
      final String apiKey, final String secret, final long timeout, final boolean followRedirects) {
    log.info("Start building new JCoinbase client !");

    jsonSerDes =
        new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new VavrModule())
            .registerModule(new JavaTimeModule())
            .setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC+01:00"))) // TODO MAKE CONFIGURABLE PARIS ZONE ID
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);

    this.properties = JCoinbasePropertiesFactory.getProperties(apiKey, secret);

    authService = new AuthenticationService();

    this.dataService = new DataService(this, new CoinbaseDataService());
    this.userService = new UserService(this, new CoinbaseUserService(), authService);

    if (followRedirects) {
      this.client =
          HttpClient.newBuilder()
              .connectTimeout(Duration.of(timeout, SECONDS))
              .followRedirects(NORMAL)
              .build();
    } else {
      this.client =
          HttpClient.newBuilder()
              .connectTimeout(Duration.of(timeout, SECONDS))
              .followRedirects(NEVER)
              .build();
    }
    log.info("JCoinbase client successfully built !");

    return this;
  }

  private void manageNotAllowed(final Throwable throwable) {
    manageOnFailure(
        new JCoinbaseException(throwable),
        "Unable to allow this request. Please make sure you correctly build your JCoinbaseClient with API KEY and SECRET",
        throwable);
  }
}
