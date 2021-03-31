package com.github.badpop.jcoinbase.model;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.data.Price.PriceType;
import io.vavr.control.Option;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.badpop.jcoinbase.model.data.Price.PriceType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PriceTest {

  @Nested
  class PriceTypeTest {
    @Test
    void should_retrieve_pricetype_as_java_optional() {
      val typeBuyLower = "buy";
      val typeBuyUpper = "BUY";

      val typeSellLower = "sell";
      val typeSellUpper = "SELL";

      val typeSpotLower = "spot";
      val typeSpotUpper = "SPOT";

      assertThat(PriceType.fromStringToOptional(typeBuyLower))
          .isInstanceOf(Optional.class)
          .contains(BUY);
      assertThat(PriceType.fromStringToOptional(typeBuyUpper))
          .isInstanceOf(Optional.class)
          .contains(BUY);

      assertThat(PriceType.fromStringToOptional(typeSellLower))
          .isInstanceOf(Optional.class)
          .contains(SELL);
      assertThat(PriceType.fromStringToOptional(typeSellUpper))
          .isInstanceOf(Optional.class)
          .contains(SELL);

      assertThat(PriceType.fromStringToOptional(typeSpotLower))
          .isInstanceOf(Optional.class)
          .contains(SPOT);
      assertThat(PriceType.fromStringToOptional(typeSpotUpper))
          .isInstanceOf(Optional.class)
          .contains(SPOT);
    }

    @Test
    void should_retrieve_pricetype_as_vavr_option() {
      val typeBuyLower = "buy";
      val typeBuyUpper = "BUY";

      val typeSellLower = "sell";
      val typeSellUpper = "SELL";

      val typeSpotLower = "spot";
      val typeSpotUpper = "SPOT";

      assertThat(PriceType.fromStringToOption(typeBuyLower))
          .isInstanceOf(Option.class)
          .contains(BUY);
      assertThat(PriceType.fromStringToOption(typeBuyUpper))
          .isInstanceOf(Option.class)
          .contains(BUY);

      assertThat(PriceType.fromStringToOption(typeSellLower))
          .isInstanceOf(Option.class)
          .contains(SELL);
      assertThat(PriceType.fromStringToOption(typeSellUpper))
          .isInstanceOf(Option.class)
          .contains(SELL);

      assertThat(PriceType.fromStringToOption(typeSpotLower))
          .isInstanceOf(Option.class)
          .contains(SPOT);
      assertThat(PriceType.fromStringToOption(typeSpotUpper))
          .isInstanceOf(Option.class)
          .contains(SPOT);
    }

    @Test
    void should_retrieve_correct_PriceType() {
      val typeBuyLower = "buy";
      val typeBuyUpper = "BUY";

      val typeSellLower = "sell";
      val typeSellUpper = "SELL";

      val typeSpotLower = "spot";
      val typeSpotUpper = "SPOT";

      assertThat(PriceType.fromString(typeBuyLower)).isEqualTo(BUY);
      assertThat(PriceType.fromString(typeBuyUpper)).isEqualTo(BUY);

      assertThat(PriceType.fromString(typeSellLower)).isEqualTo(SELL);
      assertThat(PriceType.fromString(typeSellUpper)).isEqualTo(SELL);

      assertThat(PriceType.fromString(typeSpotLower)).isEqualTo(SPOT);
      assertThat(PriceType.fromString(typeSpotUpper)).isEqualTo(SPOT);
    }

    @Test
    void should_return_empty_optional() {
      val unknownType = "Lorem Ipsum";

      val actual = PriceType.fromStringToOptional(unknownType);

      assertThat(actual).isInstanceOf(Optional.class).isEmpty();
    }

    @Test
    void should_return_empty_option() {
      val unknownType = "Lorem Ipsum";

      val actual = PriceType.fromStringToOption(unknownType);

      assertThat(actual).isInstanceOf(Option.class).isEmpty();
    }

    @Test
    void should_throws_JCoinbaseException() {
      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> PriceType.fromString("LoremIpsumDolorSitAmet"));
    }
  }
}
