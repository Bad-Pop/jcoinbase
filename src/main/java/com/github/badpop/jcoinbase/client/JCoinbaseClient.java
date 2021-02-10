package com.github.badpop.jcoinbase.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.jcoinbase.client.service.CoinbaseDataService;
import com.github.badpop.jcoinbase.client.service.DataService;
import com.github.badpop.jcoinbase.client.service.UserService;
import com.github.badpop.jcoinbase.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.properties.JCoinbasePropertiesFactory;
import io.vavr.jackson.datatype.VavrModule;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor(access = PROTECTED)
public class JCoinbaseClient {

  @Getter HttpClient client;
  @Getter ObjectMapper jsonDeserializer;
  @Getter JCoinbaseProperties properties;
  DataService dataService;
  UserService userService;

  public DataService data() {
    return dataService;
  }

  public UserService user() {
    return userService;
  }

  protected JCoinbaseClient build(
      final String apiKey, final String secret, final long timeout, final boolean followRedirects) {
    log.info("Start building new JCoinbase client !");

    jsonDeserializer =
        new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new VavrModule())
            .registerModule(new JavaTimeModule())
            .setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC+01:00"))) // PARIS ZONE ID
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);

    this.properties = JCoinbasePropertiesFactory.getProperties(apiKey, secret);

    this.dataService = new DataService(this, new CoinbaseDataService());

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
}
