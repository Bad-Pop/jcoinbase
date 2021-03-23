package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.client.service.data.CoinbaseDataService;
import com.github.badpop.jcoinbase.client.service.data.DataService;
import com.github.badpop.jcoinbase.client.service.user.CoinbaseUserService;
import com.github.badpop.jcoinbase.client.service.user.UserService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.jackson.datatype.VavrModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.ZoneId;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.net.http.HttpClient.Redirect.NEVER;
import static java.time.temporal.ChronoUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

/**
 * The JCoinbaseClient class is the main class of JCoinbase. JCoinbaseClient allows you make
 * requests to the Coinbase API in a fluent and simple way using it's api.
 *
 * <p>To build a new JCoinbaseClient object you should use the {@link JCoinbaseClientFactory}
 *
 * <p>Make request to Coinbase api using these methods :
 *
 * <ul>
 *   <li>{@link #data()} to access public data
 *   <li>{@link #user()} to access users data
 * </ul>
 */
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

  /**
   * This method provides a {@link DataService} allowing you to request coinbase public data using
   * it's api
   *
   * @return a {@link DataService}
   */
  public DataService data() {
    return dataService;
  }

  /**
   * This method provides a {@link UserService} allowing you to request coinbase public data using
   * it's api
   *
   * <p>Warning : this method throws a {@link JCoinbaseException} if you don't properly build your
   * JCoinbaseClient by providing your api key and secret
   *
   * @return a {@link UserService}
   */
  public UserService user() {
    val allowed = authService.allow(this);
    if (allowed.isLeft()) {
      manageNotAllowed(allowed.getLeft());
    }
    return userService;
  }

  /**
   * This protected method build a new JCoinbaseClient with the given parameters
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @param timeout the wanted timeout for http requests
   * @param threadSafe a boolean defining if the instance should be a thread safe singleton
   * @return a {@link JCoinbaseClient}
   */
  protected JCoinbaseClient build(
      final String apiKey,
      final String secret,
      final String apiVersion,
      final long timeout,
      final boolean threadSafe) {
    log.info("Start building new JCoinbase client !");

    buildJsonSerDes();
    buildProperties(apiKey, secret, apiVersion, threadSafe);
    buildAuthService();
    buildDataService();
    buildUserService();
    buildHttpClient(timeout);

    log.info("JCoinbase client successfully built !");

    return this;
  }

  /** Method building a new Jackson ObjectMapper with a custom configuration */
  private void buildJsonSerDes() {
    this.jsonSerDes =
        new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new VavrModule())
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Build the client properties calling the {@link JCoinbasePropertiesFactory}
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @param threadSafe a boolean defining if the instance should be a thread safe singleton
   */
  private void buildProperties(
      final String apiKey, final String secret, final String apiVersion, final boolean threadSafe) {
    this.properties = JCoinbasePropertiesFactory.build(apiKey, secret, apiVersion, threadSafe);
  }

  /** Build a new {@link AuthenticationService} */
  private void buildAuthService() {
    this.authService = new AuthenticationService();
  }

  /** Build a new {@link DataService} */
  private void buildDataService() {
    this.dataService = new DataService(this, new CoinbaseDataService());
  }

  /** Build a new {@link UserService} */
  private void buildUserService() {
    this.userService = new UserService(this, new CoinbaseUserService(), authService);
  }

  /** Build a new {@link HttpClient} */
  private void buildHttpClient(final long timeout) {
    this.httpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.of(timeout, SECONDS))
            .followRedirects(NEVER)
            .build();
  }

  /**
   * Method to call when the request to a service is not allow
   *
   * @param throwable a throwable to log
   */
  private void manageNotAllowed(final Throwable throwable) {
    ErrorManagerService.manageOnError(
        new JCoinbaseException(throwable),
        "Unable to allow this request. Please make sure you correctly build your JCoinbaseClient with API KEY and SECRET",
        throwable);
  }
}
