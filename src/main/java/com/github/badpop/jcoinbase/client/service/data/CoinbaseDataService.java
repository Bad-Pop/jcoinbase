package com.github.badpop.jcoinbase.client.service.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.data.dto.CurrencyDto;
import com.github.badpop.jcoinbase.client.service.data.dto.ExchangeRatesDto;
import com.github.badpop.jcoinbase.client.service.data.dto.PriceDto;
import com.github.badpop.jcoinbase.client.service.data.dto.TimeDto;
import com.github.badpop.jcoinbase.client.service.DataDto;
import com.github.badpop.jcoinbase.client.service.properties.JCoinbaseProperties;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import com.github.badpop.jcoinbase.model.data.Time;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AllArgsConstructor
public class CoinbaseDataService {

  private static final String ACCEPT_HEADER = "Accept";
  private static final String ACCEPT_HEADER_VALUE = "application/json";

  public Try<Time> getTime(final JCoinbaseClient client) {
    var request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl() + client.getProperties().getTimePath()))
            .header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .build();

    return Try.of(() -> client.getClient().send(request, HttpResponse.BodyHandlers.ofString()))
        .mapTry(
            stringHttpResponse ->
                client
                    .getJsonDeserializer()
                    .readValue(stringHttpResponse.body(), new TypeReference<DataDto<TimeDto>>() {})
                    .getData()
                    .toTime());
  }

  public Try<List<Currency>> getCurrencies(final JCoinbaseClient client) {

    var request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl()
                        + client.getProperties().getCurrenciesPath()))
            .header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .build();

    return Try.of(() -> client.getClient().send(request, HttpResponse.BodyHandlers.ofString()))
        .mapTry(
            stringHttpResponse ->
                client
                    .getJsonDeserializer()
                    .readValue(
                        stringHttpResponse.body(),
                        new TypeReference<DataDto<List<CurrencyDto>>>() {})
                    .getData()
                    .map(CurrencyDto::toCurrency));
  }

  public Try<ExchangeRates> getExchangeRates(final JCoinbaseClient client, final String currency) {

    var request =
        HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(
                    client.getProperties().getApiUrl()
                        + client.getProperties().getExchangeRatesPath()
                        + currency))
            .header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .build();

    return Try.of(() -> client.getClient().send(request, HttpResponse.BodyHandlers.ofString()))
        .mapTry(
            stringHttpResponse ->
                client
                    .getJsonDeserializer()
                    .readValue(
                        stringHttpResponse.body(),
                        new TypeReference<DataDto<ExchangeRatesDto>>() {})
                    .getData()
                    .toExchangeRates());
  }

  public Try<Price> getPriceByType(
      JCoinbaseClient client, PriceType priceType, String baseCurrency, String targetCurrency) {
    var request =
        HttpRequest.newBuilder()
            .GET()
            .uri(buildPriceURI(client.getProperties(), priceType, baseCurrency, targetCurrency))
            .header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .build();

    return Try.of(() -> client.getClient().send(request, HttpResponse.BodyHandlers.ofString()))
        .mapTry(
            stringHttpResponse ->
                client
                    .getJsonDeserializer()
                    .readValue(stringHttpResponse.body(), new TypeReference<DataDto<PriceDto>>() {})
                    .getData()
                    .toPrice(priceType));
  }

  private URI buildPriceURI(
      final JCoinbaseProperties properties,
      final PriceType priceType,
      final String baseCurrency,
      final String targetCurrency) {
    return URI.create(
        properties.getApiUrl()
            + properties.getPricesPath()
            + "/"
            + baseCurrency
            + "-"
            + targetCurrency
            + "/"
            + priceType.getType());
  }
}
