package com.github.badpop.jcoinbase.client.service.data;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.Currency;
import com.github.badpop.jcoinbase.model.ExchangeRates;
import com.github.badpop.jcoinbase.model.Price;
import com.github.badpop.jcoinbase.model.Price.PriceType;
import com.github.badpop.jcoinbase.model.Time;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class DataService {

  private final JCoinbaseClient client;
  private final CoinbaseDataService service;

  public Time getTime() {
    return service
        .getTime(client)
        .onSuccess(time -> log.info("Successfully fetch Time resource : {}", time))
        .onFailure(
            throwable ->
                manageOnFailure(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Time resource",
                    throwable))
        .get();
  }

  public java.util.List<Currency> getCurrenciesAsJavaList() {
    return getCurrencies().toJavaList();
  }

  public List<Currency> getCurrencies() {
    return service
        .getCurrencies(client)
        .onSuccess(currencies -> log.info("Successfully fetch Currencies resources"))
        .onFailure(
            throwable ->
                manageOnFailure(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Currencies resources",
                    throwable))
        .get();
  }

  public ExchangeRates getExchangeRates(final String currency) {
    return service
        .getExchangeRates(client, currency)
        .onSuccess(
            exchangeRates ->
                log.info("Successfully fetch Exchange rates for currency {}", currency))
        .onFailure(
            throwable ->
                manageOnFailure(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase Exchange rates for currency : {}",
                    throwable,
                    currency))
        .get();
  }

  public Price getPrice(
      final PriceType priceType, final String baseCurrency, final String targetCurrency) {

    return service
        .getPriceByType(client, priceType, baseCurrency, targetCurrency)
        .onSuccess(
            res ->
                log.info(
                    "Successfully fetch price for currency={}, targetCurrency={} and priceType={}",
                    baseCurrency,
                    targetCurrency,
                    priceType))
        .onFailure(
            throwable ->
                manageOnFailure(
                    new JCoinbaseException(throwable),
                    "An error occurred while fetching coinbase price for PriceType={}, currency{} and targetCurrency={}",
                    throwable,
                    priceType.getType(),
                    baseCurrency,
                    targetCurrency))
        .get();
  }

  private void manageOnFailure(
      final JCoinbaseException jcex,
      final String message,
      final Throwable throwable,
      final Object... logParams) {
    log.error(message, logParams, throwable);
    throw jcex;
  }
}
