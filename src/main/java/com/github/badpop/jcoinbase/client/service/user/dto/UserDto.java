package com.github.badpop.jcoinbase.client.service.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.ResourceType;
import com.github.badpop.jcoinbase.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

import static com.github.badpop.jcoinbase.model.ResourceType.UNKNOWN;
import static io.vavr.API.Option;

@Builder
@AllArgsConstructor
public class UserDto {
  private final String id;
  private final String name;
  private final String username; // Optional
  private final String profileLocation; // Optional
  private final String profileBio; // Optional
  private final String profileUrl; // Optional
  private final String avatarUrl;
  private final String resource;
  private final String resourcePath;
  private final String email;
  private final String legacyId;
  private final String timeZone;
  private final String nativeCurrency;
  private final String bitcoinUnit;
  private final String state; // Optional
  private final CountryDto country;
  private final NationalityDto nationality;
  private final boolean regionSupportsFiatTransfers;
  private final boolean regionSupportsCryptoToCryptoTransfers;
  private final Instant createdAt;
  private final boolean supportsRewards;
  private final TiersDto tiers;
  private final ReferralMoneyDto referralMoney;
  private final boolean hasBlockingBuyRestrictions;
  private final boolean hasBuyDepositPaymentMethods;
  private final boolean hasUnverifiedBuyDepositPaymentMethods;
  private final boolean needsKycRemediation;
  private final boolean showInstantAchUx;
  private final String userType;

  @JsonProperty("has_made_a_purchase")
  private final boolean hasMadeAPurchase;

  public User toUser() {
    return User.builder()
        .id(id)
        .name(name)
        .avatarUrl(avatarUrl)
        .resourceType(ResourceType.fromString(resource).getOrElse(UNKNOWN))
        .resourcePath(resourcePath)
        .email(email)
        .legacyId(legacyId)
        .timeZone(timeZone)
        .nativeCurrency(nativeCurrency)
        .bitcoinUnit(bitcoinUnit)
        .username(Option(username))
        .profileLocation(Option(profileLocation))
        .profileBio(Option(profileBio))
        .profileUrl(Option(profileUrl))
        .userType(Option(userType))
        .state(Option(state))
        .regionSupportsFiatTransfers(regionSupportsFiatTransfers)
        .regionSupportsCryptoToCryptoTransfers(regionSupportsCryptoToCryptoTransfers)
        .supportsRewards(supportsRewards)
        .hasBlockingBuyRestrictions(hasBlockingBuyRestrictions)
        .hasUnverifiedBuyDepositPaymentMethods(hasUnverifiedBuyDepositPaymentMethods)
        .hasMadeAPurchase(hasMadeAPurchase)
        .hasBuyDepositPaymentMethods(hasBuyDepositPaymentMethods)
        .needsKycRemediation(needsKycRemediation)
        .showInstantAchUx(showInstantAchUx)
        .createdAt(DateAndTimeUtils.fromInstant(createdAt).getOrNull())
        .country(Option(country).map(CountryDto::toCountry).getOrNull())
        .nationality(Option(nationality).map(NationalityDto::toNationality).getOrNull())
        .tiers(Option(tiers).map(TiersDto::toTiers).getOrNull())
        .referralMoney(Option(referralMoney).map(ReferralMoneyDto::toReferralMoney).getOrNull())
        .build();
  }
}
