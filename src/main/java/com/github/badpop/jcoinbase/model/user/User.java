package com.github.badpop.jcoinbase.model.user;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class User {
  String id;
  String name;
  String userName;
  String profileLocation; // Optional
  String profileBio; // Optional
  String profileUrl;
  String avatarUrl;
  String resource;
  String resourcePath;
  String timeZone;
  String nativeCurrency;
  String bitcoinUnits;
  Country country;
  LocalDateTime createdAt;
  String email;
}
