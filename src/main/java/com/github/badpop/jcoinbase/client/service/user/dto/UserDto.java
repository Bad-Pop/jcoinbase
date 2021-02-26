package com.github.badpop.jcoinbase.client.service.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.badpop.jcoinbase.client.service.utils.DateAndTimeUtils;
import com.github.badpop.jcoinbase.model.user.User;

import java.time.Instant;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private final String id;
  private final String name;
  private final String userName;
  private final String profileLocation; // Optional
  private final String profileBio; // Optional
  private final String profileUrl;
  private final String avatarUrl;
  private final String resource;
  private final String resourcePath;
  private final String timeZone;
  private final String nativeCurrency;
  private final String bitcoinUnits;
  private final CountryDto country;
  private final Instant createdAt;
  private final String email;

  @JsonCreator
  public UserDto(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("username") String userName,
      @JsonProperty("profile_location") String profileLocation,
      @JsonProperty("profile_bio") String profileBio,
      @JsonProperty("profile_url") String profileUrl,
      @JsonProperty("avatar_url") String avatarUrl,
      @JsonProperty("resource") String resource,
      @JsonProperty("resource_path") String resourcePath,
      @JsonProperty("time_zone") String timeZone,
      @JsonProperty("native_currency") String nativeCurrency,
      @JsonProperty("bitcoin_unit") String bitcoinUnit,
      @JsonProperty("country") CountryDto country,
      @JsonProperty("created_at") Instant createdAt,
      @JsonProperty("email") String email) {
    this.id = id;
    this.name = name;
    this.userName = userName;
    this.profileLocation = profileLocation;
    this.profileBio = profileBio;
    this.profileUrl = profileUrl;
    this.avatarUrl = avatarUrl;
    this.resource = resource;
    this.resourcePath = resourcePath;
    this.timeZone = timeZone;
    this.nativeCurrency = nativeCurrency;
    this.bitcoinUnits = bitcoinUnit;
    this.country = country;
    this.createdAt = createdAt;
    this.email = email;
  }

  public User toUser() {
    return User.builder()
        .id(id)
        .name(name)
        .userName(userName)
        .profileLocation(profileLocation)
        .profileBio(profileBio)
        .profileUrl(profileUrl)
        .avatarUrl(avatarUrl)
        .resource(resource)
        .resourcePath(resourcePath)
        .timeZone(timeZone)
        .nativeCurrency(nativeCurrency)
        .bitcoinUnits(bitcoinUnits)
        .country(country.toCountry())
        .createdAt(DateAndTimeUtils.fromInstant(createdAt))
        .email(email)
        .build();
  }
}
