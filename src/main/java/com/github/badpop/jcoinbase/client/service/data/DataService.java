package com.github.badpop.jcoinbase.client.service.data;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import com.github.badpop.jcoinbase.model.data.Time;
import com.github.badpop.jcoinbase.service.ErrorManagerService;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** This service allows you to request coinbase public data. */
@Slf4j
@AllArgsConstructor
public class DataService {

  private final JCoinbaseClient client;
  private final CoinbaseDataService service;

  /**
   * Get the Coinbase API server time.
   *
   * @return a {@link CallResult} containing a {@link Time} if it's ok, a list of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, Time> getTimeAsJava() {
    return getTime().mapFailure(Seq::asJava);
  }

  /**
   * Get the Coinbase API server time.
   *
   * @return a {@link CallResult} containing a {@link Time} if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, Time> getTime() {
    return service
        .fetchTime(client)
        .onSuccess(time -> log.info("Successfully fetch Time resource : {}", time))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Time resource",
                    throwable))
        .get();
  }

  /**
   * List Coinbase known currencies. Currency codes will conform to the ISO 4217 standard where
   * possible. Currencies which have or had no representation in ISO 4217 may use a custom code
   * (e.g. BTC).
   *
   * @return a {@link CallResult} containing a List of {@link Currency} if it's ok, a list of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, java.util.List<Currency>> getCurrenciesAsJava() {
    return getCurrencies().map(Seq::asJava).mapFailure(Seq::asJava);
  }

  /**
   * List Coinbase known currencies. Currency codes will conform to the ISO 4217 standard where
   * possible. Currencies which have or had no representation in ISO 4217 may use a custom code
   * (e.g. BTC).
   *
   * @return a {@link CallResult} containing a Seq of {@link Currency} if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, Seq<Currency>> getCurrencies() {
    return service
        .fetchCurrencies(client)
        .onSuccess(currencies -> log.info("Successfully fetch Currencies resources"))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Currencies resources",
                    throwable))
        .get();
  }

  /**
   * Get current exchange rates for the given currency.
   *
   * @param currency the currency code. For example : BTC, USD, EUR, ETH, ...
   * @return a {@link CallResult} containing an {@link ExchangeRates} object if it's ok, a List of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, ExchangeRates> getExchangeRatesAsJava(
      final String currency) {
    return getExchangeRates(currency).mapFailure(Seq::asJava);
  }

  /**
   * Get current exchange rates for the given currency.
   *
   * @param currency the currency code. For example : BTC, USD, EUR, ETH, ...
   * @return a {@link CallResult} containing an {@link ExchangeRates} object if it's ok, a Seq of
   *     {@link CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, ExchangeRates> getExchangeRates(final String currency) {
    return service
        .fetchExchangeRates(client, currency)
        .onSuccess(
            exchangeRates ->
                log.info("Successfully fetch Exchange rates for currency {}", currency))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Exchange rates for currency : {}",
                    throwable,
                    currency))
        .get();
  }

  /**
   * Get the total price to buy one currency with an other currency (e.g. BTC-USD to buy Bitcoin
   * with USD).
   *
   * @param priceType the price type to get (BUY, SELL or SPOT)
   * @param baseCurrency the base currency
   * @param targetCurrency the currency to determine price value
   * @return a {@link CallResult} containing an {@link Price} object if it's ok, a List of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<java.util.List<CoinbaseError>, Price> getPriceAsJava(
      final PriceType priceType, final String baseCurrency, final String targetCurrency) {
    return getPrice(priceType, baseCurrency, targetCurrency).mapFailure(Seq::asJava);
  }

  /**
   * Get the total price to buy one currency with an other currency (e.g. BTC-USD to buy Bitcoin
   * with USD).
   *
   * @param priceType the price type to get (BUY, SELL or SPOT)
   * @param baseCurrency the base currency
   * @param targetCurrency the currency to determine price value
   * @return a {@link CallResult} containing an {@link Price} object if it's ok, a Seq of {@link
   *     CoinbaseError} otherwise.
   * @throws JCoinbaseException on unknown errors
   */
  public CallResult<Seq<CoinbaseError>, Price> getPrice(
      final PriceType priceType, final String baseCurrency, final String targetCurrency) {
    return service
        .fetchPriceByType(client, priceType, baseCurrency, targetCurrency)
        .onSuccess(
            res ->
                log.info(
                    "Successfully fetch price for currency={}, targetCurrency={} and priceType={}",
                    baseCurrency,
                    targetCurrency,
                    priceType))
        .onFailure(
            throwable ->
                ErrorManagerService.manageOnError(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase price for PriceType={}, currency{} and targetCurrency={}",
                    throwable,
                    priceType.getType(),
                    baseCurrency,
                    targetCurrency))
        .get();
  }
}
