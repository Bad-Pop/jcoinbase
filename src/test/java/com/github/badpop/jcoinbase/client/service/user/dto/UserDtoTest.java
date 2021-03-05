package com.github.badpop.jcoinbase.client.service.user.dto;

import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.user.*;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.badpop.jcoinbase.model.user.ResourceType.UNKNOWN;
import static io.vavr.API.Option;
import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

  @Test
  @SuppressWarnings("java:S5845")
  void should_map() {
    val dto =
        UserDto.builder()
            .id("ID")
            .name("name")
            .username("username")
            .profileLocation("profileLocation")
            .profileBio("profileBio")
            .profileUrl("profileUrl")
            .avatarUrl("avatarUrl")
            .resource("user")
            .resourcePath("/v2/user")
            .email("email@email.com")
            .legacyId("legacyId")
            .timeZone("Paris")
            .nativeCurrency("EUR")
            .bitcoinUnit("BTC")
            .state("France")
            .country(CountryDto.builder().name("France").code("FR").isInEurope(true).build())
            .nationality(NationalityDto.builder().code("FR").name("France").build())
            .regionSupportsFiatTransfers(true)
            .regionSupportsCryptoToCryptoTransfers(true)
            .createdAt(Instant.parse("2017-12-11T12:38:24Z"))
            .supportsRewards(true)
            .tiers(TiersDto.builder().completedDescription("Niveau 2").build())
            .referralMoney(
                ReferralMoneyDto.builder()
                    .amount("8.30")
                    .currency("EUR")
                    .currencySymbol("€")
                    .referralThreshold("83.00")
                    .build())
            .hasBlockingBuyRestrictions(false)
            .hasMadeAPurchase(true)
            .hasBuyDepositPaymentMethods(true)
            .hasUnverifiedBuyDepositPaymentMethods(false)
            .needsKycRemediation(false)
            .showInstantAchUx(false)
            .userType("individual")
            .build();

    val actual = dto.toUser();

    assertThat(actual)
        .isEqualTo(
            User.builder()
                .id("ID")
                .name("name")
                .username(Option("username"))
                .profileLocation(Option("profileLocation"))
                .profileBio(Option("profileBio"))
                .profileUrl(Option("profileUrl"))
                .avatarUrl("avatarUrl")
                .resourceType(ResourceType.fromString("user").getOrElse(UNKNOWN))
                .resourcePath("/v2/user")
                .email("email@email.com")
                .legacyId("legacyId")
                .timeZone("Paris")
                .nativeCurrency("EUR")
                .bitcoinUnit("BTC")
                .state(Option("France"))
                .country(Country.builder().name("France").code("FR").inEurope(true).build())
                .nationality(Nationality.builder().code("FR").name("France").build())
                .regionSupportsFiatTransfers(true)
                .regionSupportsCryptoToCryptoTransfers(true)
                .createdAt(
                    DateAndTimeUtils.fromInstant(Instant.parse("2017-12-11T12:38:24Z")).getOrNull())
                .supportsRewards(true)
                .tiers(Tiers.builder().completedDescription("Niveau 2").build())
                .referralMoney(
                    ReferralMoney.builder()
                        .amount("8.30")
                        .currency("EUR")
                        .currencySymbol("€")
                        .referralThreshold("83.00")
                        .build())
                .hasBlockingBuyRestrictions(false)
                .hasMadeAPurchase(true)
                .hasBuyDepositPaymentMethods(true)
                .hasUnverifiedBuyDepositPaymentMethods(false)
                .needsKycRemediation(false)
                .showInstantAchUx(false)
                .userType(Option("individual"))
                .build());
  }
}
