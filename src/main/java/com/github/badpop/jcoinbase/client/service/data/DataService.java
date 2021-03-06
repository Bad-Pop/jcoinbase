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

@Slf4j
@AllArgsConstructor
public class DataService {

  private final JCoinbaseClient client;
  private final CoinbaseDataService service;

  public CallResult<java.util.List<CoinbaseError>, Time> getTimeAsJava() {
    return getTime().mapFailure(Seq::asJava);
  }

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

  // TODO REFACTOR CURRENT IMPL WITH CALLRESULT
  public java.util.List<Currency> getCurrenciesAsJava() {
    return getCurrencies().toJavaList();
  }

  // TODO REFACTOR CURRENT IMPL WITH CALLRESULT
  public Seq<Currency> getCurrencies() {
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

  // TODO REFACTOR CURRENT IMPL WITH CALLRESULT
  public ExchangeRates getExchangeRates(final String currency) {
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

  // TODO REFACTOR CURRENT IMPL WITH CALLRESULT
  public Price getPrice(
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
