package com.github.badpop.jcoinbase.client.service.data;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Time;
import io.vavr.control.Try;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static io.vavr.API.List;
import static io.vavr.API.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

  @InjectMocks private DataService dataService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseDataService coinbaseDataService;

  @Test
  void should_return_time() {

    val localDT = LocalDateTime.of(2021, 2, 7, 15, 30);
    val time =
        Time.builder()
            .iso(localDT)
            .epoch(localDT.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(localDT)))
            .build();

    when(coinbaseDataService.fetchTime(client)).thenReturn(Try.success(time));

    val actual = dataService.fetchTime();

    assertThat(actual).isEqualTo(time);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchTime(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getTime_should_throw_JCoinbaseException() {

    val throwable = new Exception("Error message");

    when(coinbaseDataService.fetchTime(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.fetchTime())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchTime(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrenciesAsJavaList_should_return_currencies_as_java_list() {

    val eur = Currency.builder().id("EUR").name("Euro").minSize(BigDecimal.valueOf(0.01)).build();
    val usd =
        Currency.builder()
            .id("USD")
            .name("United States Dollar")
            .minSize(BigDecimal.valueOf(0.01))
            .build();

    when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.success(List(eur, usd)));

    val actual = dataService.fetchCurrenciesAsJavaList();

    assertThat(actual).containsExactly(eur, usd);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrenciesAsJavaList_should_throw_JCoinbaseException() {
    val throwable = new Exception("Error message");
    when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.fetchCurrenciesAsJavaList())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrencies_should_return_currencies_as_vavr_list() {

    val eur = Currency.builder().id("EUR").name("Euro").minSize(BigDecimal.valueOf(0.01)).build();
    val usd =
        Currency.builder()
            .id("USD")
            .name("United States Dollar")
            .minSize(BigDecimal.valueOf(0.01))
            .build();

    when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.success(List(eur, usd)));

    val actual = dataService.fetchCurrencies();

    VavrAssertions.assertThat(actual).containsExactly(eur, usd);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getCurrencies_should_throw_JCoinbaseException() {
    val throwable = new Exception("Error message");
    when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.fetchCurrencies())
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchCurrencies(client);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getExchangeRates_should_return_ExchangeRates() {

    val currency = "BTC";
    val rates =
        Map(
            "EUR", BigDecimal.valueOf(32000.47),
            "USD", BigDecimal.valueOf(39000.09));

    val exchangeRates = ExchangeRates.builder().currency(currency).rates(rates).build();

    when(coinbaseDataService.fetchExchangeRates(client, currency))
        .thenReturn(Try.success(exchangeRates));

    val actual = dataService.fetchExchangeRates(currency);

    assertThat(actual).isEqualTo(exchangeRates);
    assertThat(actual.getRates()).containsExactlyElementsOf(rates);
    assertThat(actual.getRatesAsJavaMap()).containsExactlyEntriesOf(rates.toJavaMap());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchExchangeRates(client, currency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getExchangeRates_should_throw_JCoinbaseException() {
    val currency = "BTC";
    val throwable = new Exception("Error message");
    when(coinbaseDataService.fetchExchangeRates(client, currency))
        .thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.fetchExchangeRates(currency))
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchExchangeRates(client, currency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_BUY_PRICE() {

    val priceType = BUY;
    val baseCurrency = "BTC";
    val targetCurrency = "EUR";

    val price = Price.builder().build();

    when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    val actual = dataService.fetchPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_SELL_PRICE() {
    val priceType = SELL;
    val baseCurrency = "BTC";
    val targetCurrency = "EUR";

    val price = Price.builder().build();

    when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    val actual = dataService.fetchPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_return_currency_SPOT_PRICE() {
    val priceType = SPOT;
    val baseCurrency = "BTC";
    val targetCurrency = "EUR";

    val price = Price.builder().build();

    when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.success(price));

    val actual = dataService.fetchPrice(priceType, baseCurrency, targetCurrency);

    assertThat(actual).isNotNull().isEqualTo(price);

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }

  @Test
  void getPrice_should_throw_JCoinbaseException() {

    val throwable = new Exception("Error message");
    val priceType = SPOT;
    val baseCurrency = "BTC";
    val targetCurrency = "EUR";

    when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
        .thenReturn(Try.failure(throwable));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> dataService.fetchPrice(priceType, baseCurrency, targetCurrency))
        .withMessage("java.lang.Exception: " + throwable.getMessage());

    verifyNoInteractions(client);
    verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
    verifyNoMoreInteractions(coinbaseDataService);
  }
}
