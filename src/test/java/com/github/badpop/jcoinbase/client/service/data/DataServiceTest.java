package com.github.badpop.jcoinbase.client.service.data;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.data.Currency;
import com.github.badpop.jcoinbase.model.data.ExchangeRates;
import com.github.badpop.jcoinbase.model.data.Price;
import com.github.badpop.jcoinbase.model.data.Time;
import com.github.badpop.jcoinbase.testutils.CoinbaseErrorSampleProvider;
import io.vavr.control.Try;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

  @InjectMocks private DataService dataService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseDataService coinbaseDataService;

  @Nested
  class GetTime {

    @Test
    void should_return_time() {
      val localDT = LocalDateTime.of(2021, 2, 7, 15, 30);
      val time =
          Time.builder()
              .iso(localDT)
              .epoch(
                  localDT.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(localDT)))
              .build();

      when(coinbaseDataService.fetchTime(client)).thenReturn(Try.success(CallResult.success(time)));

      val actual = dataService.getTime();

      assertThat(actual).isNotEmpty().isInstanceOf(CallResult.class);
      assertThat(actual.get()).isEqualTo(time);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchTime(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_failure() {
      when(coinbaseDataService.fetchTime(client))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getTime();

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchTime(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_failure_as_java() {
      when(coinbaseDataService.fetchTime(client))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getTimeAsJava();

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure())
          .isInstanceOf(java.util.List.class)
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchTime(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void getTime_should_throw_JCoinbaseException() {

      val throwable = new Exception("Error message");

      when(coinbaseDataService.fetchTime(client)).thenReturn(Try.failure(throwable));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> dataService.getTime())
          .withMessage("java.lang.Exception: " + throwable.getMessage());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchTime(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }
  }

  @Nested
  class GetCurrencies {

    @Test
    void should_return_callresult_failure() {
      when(coinbaseDataService.fetchCurrencies(client))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getCurrencies();

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchCurrencies(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_callresult_failure_as_java() {
      when(coinbaseDataService.fetchCurrencies(client))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getCurrenciesAsJava();

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).isInstanceOf(java.util.List.class);
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchCurrencies(client);
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

      when(coinbaseDataService.fetchCurrencies(client))
          .thenReturn(Try.success(CallResult.success(List(eur, usd))));

      val actual = dataService.getCurrenciesAsJava();

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty();
      assertThat(actual.get()).isInstanceOf(java.util.List.class);
      assertThat(actual.get()).containsExactly(eur, usd);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchCurrencies(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void getCurrenciesAsJavaList_should_throw_JCoinbaseException() {
      val throwable = new Exception("Error message");
      when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.failure(throwable));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> dataService.getCurrenciesAsJava())
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

      when(coinbaseDataService.fetchCurrencies(client))
          .thenReturn(Try.success(CallResult.success(List(eur, usd))));

      val actual = dataService.getCurrencies();

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty();
      VavrAssertions.assertThat(actual.get()).containsExactly(eur, usd);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchCurrencies(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void getCurrencies_should_throw_JCoinbaseException() {
      val throwable = new Exception("Error message");
      when(coinbaseDataService.fetchCurrencies(client)).thenReturn(Try.failure(throwable));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> dataService.getCurrencies())
          .withMessage("java.lang.Exception: " + throwable.getMessage());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchCurrencies(client);
      verifyNoMoreInteractions(coinbaseDataService);
    }
  }

  @Nested
  class GetExchangeRates {

    @Test
    void getExchangeRates_should_return_ExchangeRates() {

      val currency = "BTC";
      val rates =
          Map(
              "EUR", BigDecimal.valueOf(32000.47),
              "USD", BigDecimal.valueOf(39000.09));

      val exchangeRates = ExchangeRates.builder().currency(currency).rates(rates).build();

      when(coinbaseDataService.fetchExchangeRates(client, currency))
          .thenReturn(Try.success(CallResult.success(exchangeRates)));

      val actual = dataService.getExchangeRates(currency);

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty();

      assertThat(actual.get()).isEqualTo(exchangeRates);
      assertThat(actual.get().getRates()).containsExactlyElementsOf(rates);
      assertThat(actual.get().getRatesAsJavaMap()).containsExactlyEntriesOf(rates.toJavaMap());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchExchangeRates(client, currency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_call_result_failure() {

      val currency = "BTC";

      when(coinbaseDataService.fetchExchangeRates(client, currency))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getExchangeRates(currency);

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchExchangeRates(client, currency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_call_result_failure_as_java() {

      val currency = "BTC";

      when(coinbaseDataService.fetchExchangeRates(client, currency))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getExchangeRatesAsJava(currency);

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure())
          .isInstanceOf(java.util.List.class)
          .containsExactly(CoinbaseErrorSampleProvider.getSingleError());

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
          .isThrownBy(() -> dataService.getExchangeRates(currency))
          .withMessage("java.lang.Exception: " + throwable.getMessage());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchExchangeRates(client, currency);
      verifyNoMoreInteractions(coinbaseDataService);
    }
  }

  @Nested
  class GetPrices {
    @Test
    void should_return_currency_BUY_PRICE() {

      val priceType = BUY;
      val baseCurrency = "BTC";
      val targetCurrency = "EUR";

      val price = Price.builder().build();

      when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
          .thenReturn(Try.success(CallResult.success(price)));

      val actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

      assertThat(actual).isNotEmpty().isInstanceOf(CallResult.class);
      assertThat(actual.get()).isNotNull().isEqualTo(price);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_currency_SELL_PRICE() {
      val priceType = SELL;
      val baseCurrency = "BTC";
      val targetCurrency = "EUR";

      val price = Price.builder().build();

      when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
          .thenReturn(Try.success(CallResult.success(price)));

      val actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

      assertThat(actual).isNotEmpty().isInstanceOf(CallResult.class);
      assertThat(actual.get()).isNotNull().isEqualTo(price);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_currency_SPOT_PRICE() {
      val priceType = SPOT;
      val baseCurrency = "BTC";
      val targetCurrency = "EUR";

      val price = Price.builder().build();

      when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
          .thenReturn(Try.success(CallResult.success(price)));

      val actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

      assertThat(actual).isNotEmpty().isInstanceOf(CallResult.class);
      assertThat(actual.get()).isNotNull().isEqualTo(price);

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_callresult_failure() {
      val priceType = SPOT;
      val baseCurrency = "BTC";
      val targetCurrency = "EUR";

      when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getPrice(priceType, baseCurrency, targetCurrency);

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
      verifyNoMoreInteractions(coinbaseDataService);
    }

    @Test
    void should_return_callresult_failure_as_java() {
      val priceType = SPOT;
      val baseCurrency = "BTC";
      val targetCurrency = "EUR";

      when(coinbaseDataService.fetchPriceByType(client, priceType, baseCurrency, targetCurrency))
          .thenReturn(
              Try.success(CallResult.failure(Seq(CoinbaseErrorSampleProvider.getSingleError()))));

      val actual = dataService.getPriceAsJava(priceType, baseCurrency, targetCurrency);

      assertThat(actual).isInstanceOf(CallResult.class).isEmpty();
      assertThat(actual.getFailure()).isInstanceOf(java.util.List.class);
      assertThat(actual.getFailure()).containsExactly(CoinbaseErrorSampleProvider.getSingleError());

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
          .isThrownBy(() -> dataService.getPrice(priceType, baseCurrency, targetCurrency))
          .withMessage("java.lang.Exception: " + throwable.getMessage());

      verifyNoInteractions(client);
      verify(coinbaseDataService).fetchPriceByType(client, priceType, baseCurrency, targetCurrency);
      verifyNoMoreInteractions(coinbaseDataService);
    }
  }
}
