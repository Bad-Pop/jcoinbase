package com.github.badpop.jcoinbase.model.data;

import com.github.badpop.jcoinbase.exception.PriceTypeNotFoundException;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Optional;

import static io.vavr.API.List;
import static lombok.AccessLevel.PRIVATE;

/** Class representing the Coinbase price model */
@Value
@Builder
public class Price {

  String baseCurrency;
  String targetCurrency;
  BigDecimal amount;
  PriceType priceType;

  /**
   * There is three types of prices in Coinbase : BUY, SELL and SPOT. This enum allow us to manage
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
     * Retrieve a price type from a string representation. This method return an Optional containing
     * the founded PriceType, otherwise the Optional will be empty
     *
     * @param type the string representation of the price type as given by the coinbase api
     * @return an Optional containing the founded PriceType or an empty Optional
     */
    public static Optional<PriceType> fromStringToOptional(final String type) {
      return fromStringToOption(type).toJavaOptional();
    }

    /**
     * Retrieve a price type from a string representation. This method return an Option containing *
     * the founded PriceType, otherwise the Option will be empty
     *
     * @param type the string representation of the price type as given by the coinbase api
     * @return a Vavr Option containing the founded price type or an empty Option
     */
    public static Option<PriceType> fromStringToOption(final String type) {
      return priceTypes.find(priceType -> priceType.getType().equalsIgnoreCase(type));
    }

    /**
     * Retrieve a price type from a string representation. This method return the PriceType if
     * exists or throws exception otherwise
     *
     * @param type the string representation of the price type as given by the coinbase api
     * @return the founded PriceType or throws a {@link PriceTypeNotFoundException}
     */
    public static PriceType fromString(final String type) {
      return priceTypes
          .find(priceType -> priceType.getType().equalsIgnoreCase(type))
          .getOrElseThrow(
              () ->
                  new PriceTypeNotFoundException(
                      "Unable to find price type. Available values : "
                          + priceTypes.map(PriceType::getType).toJavaList().toString()));
    }
  }
}
