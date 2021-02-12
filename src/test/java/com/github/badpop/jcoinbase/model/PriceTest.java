package com.github.badpop.jcoinbase.model;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import org.junit.jupiter.api.Test;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PriceTest {

  @Test
  void should_retrieve_correct_PriceType() {
    var typeBuyLower = "buy";
    var typeBuyUpper = "BUY";

    var typeSellLower = "sell";
    var typeSellUpper = "SELL";

    var typeSpotLower = "spot";
    var typeSpotUpper = "SPOT";

    assertThat(PriceType.fromString(typeBuyLower)).isEqualTo(BUY);
    assertThat(PriceType.fromString(typeBuyUpper)).isEqualTo(BUY);

    assertThat(PriceType.fromString(typeSellLower)).isEqualTo(SELL);
    assertThat(PriceType.fromString(typeSellUpper)).isEqualTo(SELL);

    assertThat(PriceType.fromString(typeSpotLower)).isEqualTo(SPOT);
    assertThat(PriceType.fromString(typeSpotUpper)).isEqualTo(SPOT);
  }

  @Test
  void should_throw_JCoinbaseException() {
    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> PriceType.fromString("LoremIpsumDolorSitAmet"));
  }
}
