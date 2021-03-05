package com.github.badpop.jcoinbase.model.user;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.vavr.API.None;

@Value
@Builder
public class User {
  String id;
  String name;
  String avatarUrl;
  ResourceType resourceType;
  String resourcePath;
  String email;
  String legacyId;
  String timeZone;
  String nativeCurrency;
  String bitcoinUnit;
  @Default Option<String> username = None();
  @Default Option<String> profileLocation = None();
  @Default Option<String> profileBio = None();
  @Default Option<String> profileUrl = None();
  @Default Option<String> userType = None();
  @Default Option<String> state = None();

  boolean regionSupportsFiatTransfers;
  boolean regionSupportsCryptoToCryptoTransfers;
  boolean supportsRewards;
  boolean hasBlockingBuyRestrictions;
  boolean hasBuyDepositPaymentMethods;
  boolean hasUnverifiedBuyDepositPaymentMethods;
  boolean hasMadeAPurchase;
  boolean needsKycRemediation;
  boolean showInstantAchUx;

  LocalDateTime createdAt;

  Country country;
  Nationality nationality;
  Tiers tiers;
  ReferralMoney referralMoney;

  public Optional<String> getUsernameAsJavaOptional() {
    return username.toJavaOptional();
  }

  public Optional<String> getProfileLocationAsJavaOptional() {
    return profileLocation.toJavaOptional();
  }

  public Optional<String> getProfileBioAsJavaOptional() {
    return profileBio.toJavaOptional();
  }

  public Optional<String> getProfileUrlAsJavaOptional() {
    return profileUrl.toJavaOptional();
  }

  public Optional<String> getUserTypeAsJavaOptional() {
    return userType.toJavaOptional();
  }

  public Optional<String> getStateAsJavaOptional() {
    return state.toJavaOptional();
  }
}
