package com.github.badpop.jcoinbase.model.data;

import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

import static io.vavr.API.List;
import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
public class Price {

  String baseCurrency;
  String targetCurrency;
  BigDecimal amount;
  PriceType priceType;

  @Getter
  @AllArgsConstructor(access = PRIVATE)
  public enum PriceType {
    BUY("buy"),
    SELL("sell"),
    SPOT("spot");

    private static final List<PriceType> priceTypes = List(values());
    private final String type;

    public static PriceType fromString(final String type) {
      return priceTypes
          .find(priceType -> priceType.getType().equalsIgnoreCase(type))
          .getOrElseThrow(
              () ->
                  new JCoinbaseException(
                      "Unable to find price type. Available values : "
                          + priceTypes.map(PriceType::getType).toJavaList().toString()));
    }
  }
}
