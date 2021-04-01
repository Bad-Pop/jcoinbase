package com.github.badpop.jcoinbase.service.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.JCoinbaseProperties;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import com.github.badpop.jcoinbase.model.data.Time;
import com.github.badpop.jcoinbase.service.data.dto.CurrencyDto;
import com.github.badpop.jcoinbase.service.data.dto.ExchangeRatesDto;
import com.github.badpop.jcoinbase.service.data.dto.PriceDto;
import com.github.badpop.jcoinbase.service.data.dto.TimeDto;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.http.HttpRequestSender;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;

import static com.github.badpop.jcoinbase.service.http.Headers.ACCEPT;
import static com.github.badpop.jcoinbase.service.http.Headers.ACCEPT_VALUE;

public class CoinbaseDataService {

  protected Try<CallResult<Seq<CoinbaseError>, Time>> fetchTime(final JCoinbaseClient client) {
    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl() + client.getProperties().getTimePath()))
            .header(ACCEPT.getValue(), ACCEPT_VALUE.getValue())
            .build();

    return HttpRequestSender.singleFailureSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<TimeDto>>() {})
        .mapTry(callResult -> callResult.map(data -> data.getData().toTime()));
  }

  protected Try<CallResult<Seq<CoinbaseError>, Seq<Currency>>> fetchCurrencies(
      final JCoinbaseClient client) {

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl()
                        + client.getProperties().getCurrenciesPath()))
            .header(ACCEPT.getValue(), ACCEPT_VALUE.getValue())
            .build();

    return HttpRequestSender.singleFailureSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<List<CurrencyDto>>>() {})
        .mapTry(
            callResult ->
                callResult
                    .map(DataDto::getData)
                    .map(currencies -> currencies.map(CurrencyDto::toCurrency)));
  }

  protected Try<CallResult<Seq<CoinbaseError>, ExchangeRates>> fetchExchangeRates(
      final JCoinbaseClient client, final String currency) {

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl()
                        + client.getProperties().getExchangeRatesPath()
                        + currency))
            .header(ACCEPT.getValue(), ACCEPT_VALUE.getValue())
            .build();

    return HttpRequestSender.singleFailureSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<ExchangeRatesDto>>() {})
        .mapTry(
            callResult -> callResult.map(DataDto::getData).map(ExchangeRatesDto::toExchangeRates));
  }

  protected Try<CallResult<Seq<CoinbaseError>, Price>> fetchPriceByType(
      JCoinbaseClient client, PriceType priceType, String baseCurrency, String targetCurrency) {

    val request =
        HttpRequest.newBuilder()
            .GET()
            .uri(buildPriceURI(client.getProperties(), priceType, baseCurrency, targetCurrency))
            .header(ACCEPT.getValue(), ACCEPT_VALUE.getValue())
            .build();

    return HttpRequestSender.singleFailureSend(
            client.getHttpClient(),
            request,
            client.getJsonSerDes(),
            new TypeReference<DataDto<PriceDto>>() {})
        .mapTry(
            callResult -> callResult.map(DataDto::getData).map(price -> price.toPrice(priceType)));
  }

  private URI buildPriceURI(
      final JCoinbaseProperties properties,
      final PriceType priceType,
      final String baseCurrency,
      final String targetCurrency) {
    return URI.create(
        String.format(
            "%s%s/%s/%s",
            properties.getApiUrl(),
            properties.getPricesPath(),
            baseCurrency + "-" + targetCurrency,
            priceType.getType()));
  }
}
