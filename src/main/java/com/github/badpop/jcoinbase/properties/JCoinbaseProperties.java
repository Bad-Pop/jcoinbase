package com.github.badpop.jcoinbase.properties;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
@Getter
@Builder
@ToString
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class JCoinbaseProperties {

  final Properties properties = new Properties();

  Option<String> apiKey;
  Option<String> secret;

  // With privileges
  String apiUrl;
  String currentUserPath;
  String currentUserAuthorizationsPath;
  String userPath;
  String accountPath;
  String paymentMethodPath;

  // Without privileges
  String currenciesPath;
  String exchangeRatesPath;
  String pricesPath;
  String timePath;

  protected JCoinbaseProperties build(final String apiKey, final String secret) {

    log.info("Start building JCoinbase properties !");
    var inputStreamProperties =
        Try.of(() -> this.getClass().getClassLoader().getResourceAsStream("jcoinbase.properties"))
            .onFailure(
                throwable -> {
                  log.error("Unable to build inputStream for JCoinbase properties file", throwable);
                  throw new JCoinbaseException(
                      "Unable to build inputStream for JCoinbase properties file.", throwable);
                });

    inputStreamProperties
        .peek(
            inputStream -> {
              if (inputStream == null)
                throw new JCoinbaseException("Unable to find JCoinbase properties file.");
            })
        .mapTry(
            inputStream -> {
              properties.load(inputStream);
              extractProperties(apiKey, secret);
              return properties;
            })
        .onFailure(
            JCoinbaseException.class,
            jCoinbaseException -> {
              log.error(jCoinbaseException.getMessage());
              throw jCoinbaseException;
            })
        .onFailure(
            throwable -> {
              log.error(
                  "An unknown error occurred while building JCoinbase properties.", throwable);
              throw new JCoinbaseException(
                  "An unknown error occurred while building JCoinbase properties.", throwable);
            });
    log.info("JCoinbase properties successfully built !");
    return this;
  }

  private void extractProperties(final String apiKey, final String secret) {

    this.apiKey = Option.of(apiKey);
    this.secret = Option.of(secret);

    this.apiUrl = properties.getProperty("coinbase.api.url");
    this.currentUserPath = properties.getProperty("coinbase.api.path.resource.currentUser");
    this.currentUserAuthorizationsPath =
        properties.getProperty("coinbase.api.path.resource.currentUserAuthorizations");
    this.userPath = properties.getProperty("coinbase.api.path.resource.user");
    this.accountPath = properties.getProperty("coinbase.api.path.resource.account");
    this.paymentMethodPath = properties.getProperty("coinbase.api.path.resource.paymentMethods");

    this.currenciesPath = properties.getProperty("coinbase.api.path.resource.currencies");
    this.exchangeRatesPath = properties.getProperty("coinbase.api.path.resource.exchangeRates");
    this.pricesPath = properties.getProperty("coinbase.api.path.resource.prices");
    this.timePath = properties.getProperty("coinbase.api.path.resource.time");
  }
}
