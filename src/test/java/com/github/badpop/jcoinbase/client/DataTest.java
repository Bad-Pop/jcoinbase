package com.github.badpop.jcoinbase.client;

import com.github.badpop.jcoinbase.client.service.CoinbaseDataService;
import com.github.badpop.jcoinbase.client.service.DataService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.Currency;
import com.github.badpop.jcoinbase.model.ExchangeRates;
import com.github.badpop.jcoinbase.model.Price;
import com.github.badpop.jcoinbase.model.Time;
import io.vavr.control.Try;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.github.badpop.jcoinbase.model.Price.PriceType.*;
import static io.vavr.API.List;
import static io.vavr.API.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataTest {

  @InjectMocks private DataService dataService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseDataService coinbaseDataService;

  @Test
  void getTime_should_return_time() {

    var localDT = LocalDateTime.of(2021, 2, 7, 15, 30);
    var time =
        Time.builder()
            .iso(localDT)
            .epoch(localDT.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(localDT)))
            .build();

    when(coinbaseDataService.getTime(client)).thenReturn(Try.success(time));

    var actual = dataService.getTime();

    assertThat(actual).isEqualTo(time);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getTime(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getTime_should_throw_JCoinbaseException() {

    var throwable = new Exception("Error message");

    when(coinbaseDataService.getTime(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.getTime())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getTime(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrenciesAsJavaList_should_return_currencies_as_java_list() {

    var eur = Currency.builder().id("EUR").name("Euro").minSize(BigDecimal.valueOf(0.01)).build();
    var usd =
        Currency.builder()
            .id("USD")
            .name("United States Dollar")
            .minSize(BigDecimal.valueOf(0.01))
            .build();

    when(coinbaseDataService.getCurrencies(client)).thenReturn(Try.success(List(eur, usd)));

    var actual = dataService.getCurrenciesAsJavaList();

    assertThat(actual).containsExactly(eur, usd);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrenciesAsJavaList_should_throw_JCoinbaseException() {
    var throwable = new Exception("Error message");
    when(coinbaseDataService.getCurrencies(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.getCurrenciesAsJavaList())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrencies_should_return_currencies_as_vavr_list() {

    var eur = Currency.builder().id("EUR").name("Euro").minSize(BigDecimal.valueOf(0.01)).build();
    var usd =
        Currency.builder()
            .id("USD")
            .name("United States Dollar")
            .minSize(BigDecimal.valueOf(0.01))
            .build();

    when(coinbaseDataService.getCurrencies(client)).thenReturn(Try.success(List(eur, usd)));

    var actual = dataService.getCurrencies();

    VavrAssertions.assertThat(actual).containsExactly(eur, usd);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrencies_should_throw_JCoinbaseException() {
    var throwable = new Exception("Error message");
    when(coinbaseDataService.getCurrencies(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.getCurrencies())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getExchangeRates_should_return_ExchangeRates() {

    var currency = "BTC";
    var rates =
        Map(
            "EUR", BigDecimal.valueOf(32000.47),
            "USD", BigDecimal.valueOf(39000.09));

    var exchangeRates = ExchangeRates.builder().currency(currency).rates(rates).build();

    when(coinbaseDataService.getExchangeRates(client, currency)).thenReturn(Try.success(exchangeRates));

    var actual = dataService.getExchangeRates(currency);

    assertThat(actual).isEqualTo(exchangeRates);
    assertThat(actual.getRates()).containsExactlyElementsOf(rates);
    assertThat(actual.getRatesAsJavaMap()).containsExactlyEntriesOf(rates.toJavaMap());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getExchangeRates(client, currency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getExchangeRates_should_throw_JCoinbaseException() {
    var currency = "BTC";
    var throwable = new Exception("Error message");
    when(coinbaseDataService.getExchangeRates(client, currency)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.getExchangeRates(currency))
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getExchangeRates(client, currency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_BUY_PRICE() {

    var priceType = BUY;
    var baseCurrency = "BTC";
    var targetCurrency = "EUR";

    var price = Price.builder().build();

    when(coinbaseDataService.getPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    var actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_SELL_PRICE() {
    var priceType = SELL;
    var baseCurrency = "BTC";
    var targetCurrency = "EUR";

    var price = Price.builder().build();

    when(coinbaseDataService.getPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    var actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_SPOT_PRICE() {
    var priceType = SPOT;
    var baseCurrency = "BTC";
    var targetCurrency = "EUR";

    var price = Price.builder().build();

    when(coinbaseDataService.getPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    var actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).getPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_throw_JCoinbaseException() {

    var throwable = new Exception("Error message");
    var priceType = SPOT;
    var baseCurrency = "BTC";
    var targetCurrency = "EUR";

    when(coinbaseDataService.getPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
            .isThrownBy(() -> dataService.getPrice(priceType, baseCurrency, targetCurrency))
            .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).getPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }
}
