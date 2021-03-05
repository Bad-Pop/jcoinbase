package com.github.badpop.jcoinbase.model.user;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.vavr.API.Option;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

  @Test
  void should_return_vavr_options_as_java_optionals() {
    var user =
        User.builder()
            .username(Option("username"))
            .profileLocation(Option("profileLocation"))
            .profileBio(Option("profileBio"))
            .profileUrl(Option("profileUrl"))
            .userType(Option("userType"))
            .state(Option("state"))
            .build();

    var actualUsername = user.getUsernameAsJavaOptional();
    var actualProfileLocation = user.getProfileLocationAsJavaOptional();
    var actualProfileBio = user.getProfileBioAsJavaOptional();
    var actualProfileUrl = user.getProfileUrlAsJavaOptional();
    var actualUserType = user.getUserTypeAsJavaOptional();
    var actualState = user.getStateAsJavaOptional();

    assertThat(actualUsername).isInstanceOf(Optional.class).contains(user.getUsername().get());
    assertThat(actualProfileLocation)
        .isInstanceOf(Optional.class)
        .contains(user.getProfileLocation().get());
    assertThat(actualProfileBio).isInstanceOf(Optional.class).contains(user.getProfileBio().get());
    assertThat(actualProfileUrl).isInstanceOf(Optional.class).contains(user.getProfileUrl().get());
    assertThat(actualUserType).isInstanceOf(Optional.class).contains(user.getUserType().get());
    assertThat(actualState).isInstanceOf(Optional.class).contains(user.getState().get());
  }
}
