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

/** A class representing the Coinbase price model */
@Value
@Builder
public class Price {

  String baseCurrency;
  String targetCurrency;
  BigDecimal amount;
  PriceType priceType;

  /**
   * There is three types of prices in Coinbase : BUY, SELL & SPOT. This enum allow us to manage
   * these types in a more fluent way than simple strings representations.
   */
  @Getter
  @AllArgsConstructor(access = PRIVATE)
  public enum PriceType {
    BUY("buy"),
    SELL("sell"),
    SPOT("spot");

    private static final List<PriceType> priceTypes = List(values());
    private final String type;

    /**
     * Retrieve a price type from a string representation. This method return an Option containing
     * the founded PriceType, otherwise the Option will be empty
     *
     * @param type the string representation of the price type as given by the coinbase api
     * @return an Option containing the PriceType or empty
     */
    // TODO REFACTOR to allow retrieve as java Optional
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
