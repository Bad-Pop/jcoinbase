package com.github.badpop.jcoinbase;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.exception.PropertiesNotFoundException;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

/**
 * This class is used to get properties from the jcoinbase.properties file and to wrap them with
 * user parameters (api key, secret, api version, ...). Thus the data are centralized in this class.
 */
@Slf4j
@Getter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class JCoinbaseProperties {

  final Properties properties = new Properties();

  Option<String> apiKey;
  Option<String> secret;
  Option<String> apiVersion;

  // With authentication
  String apiUrl;
  String currentUserPath;
  String currentUserAuthorizationsPath;
  String userPath;
  String usersPath;
  String accountsPath;
  String paymentMethodPath;

  // Without authentication
  String currenciesPath;
  String exchangeRatesPath;
  String pricesPath;
  String timePath;

  /**
   * Call this method to build a properly configured JCoinbaseProperties.
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   * @return a new configured {@link JCoinbaseProperties}
   */
  protected JCoinbaseProperties build(
      final String apiKey, final String secret, final String apiVersion) {

    log.info("Start building JCoinbase properties !");
    val inputStreamProperties =
        Try.of(() -> this.getClass().getClassLoader().getResourceAsStream("jcoinbase.properties"))
            .onFailure(
                throwable ->
                    ErrorManagerService.manageOnError(
                        new JCoinbaseException(
                            "Unable to build inputStream for JCoinbase properties file.",
                            throwable),
                        "Unable to build inputStream for JCoinbase properties file",
                        throwable));

    inputStreamProperties
        .peek(
            inputStream -> {
              if (inputStream == null)
                ErrorManagerService.manageOnError(
                    new PropertiesNotFoundException("Unable to find JCoinbase properties file."),
                    "Unable to find JCoinbase properties file.");
            })
        .mapTry(
            inputStream -> {
              properties.load(inputStream);
              extractProperties(apiKey, secret, apiVersion);
              return properties;
            })
        .onFailure(
            JCoinbaseException.class,
            jCoinbaseException ->
                ErrorManagerService.manageOnError(
                    jCoinbaseException, jCoinbaseException.getMessage()))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(
                        "An unknown error occurred while building JCoinbase properties.",
                        throwable),
                    "An unknown error occurred while building JCoinbase properties.",
                    throwable));
    log.info("JCoinbase properties successfully built !");
    return this;
  }

  /**
   * A simple method used to extract properties from properties file and put them in the class'
   * fields
   *
   * @param apiKey the coinbase api key
   * @param secret the coinbase api secret
   * @param apiVersion the coinbase api version
   */
  private void extractProperties(
      final String apiKey, final String secret, final String apiVersion) {

    this.apiKey = Option.of(apiKey);
    this.secret = Option.of(secret);
    this.apiVersion = Option.of(apiVersion);

    this.apiUrl = properties.getProperty("coinbase.api.url");
    this.currentUserPath = properties.getProperty("coinbase.api.path.resource.currentUser");
    this.currentUserAuthorizationsPath =
        properties.getProperty("coinbase.api.path.resource.currentUserAuthorizations");
    this.userPath = properties.getProperty("coinbase.api.path.resource.user");
    this.usersPath = properties.getProperty("coinbase.api.path.resource.users");
    this.accountsPath = properties.getProperty("coinbase.api.path.resource.accounts");
    this.paymentMethodPath = properties.getProperty("coinbase.api.path.resource.paymentMethods");

    this.currenciesPath = properties.getProperty("coinbase.api.path.resource.currencies");
    this.exchangeRatesPath = properties.getProperty("coinbase.api.path.resource.exchangeRates");
    this.pricesPath = properties.getProperty("coinbase.api.path.resource.prices");
    this.timePath = properties.getProperty("coinbase.api.path.resource.time");
  }
}
