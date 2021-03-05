package com.github.badpop.jcoinbase.client.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Country;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryDtoTest {

  @Test
  void should_map_to_country() {
    val code = "code";
    val name = "name";
    val isInEurope = true;
    val dto = CountryDto.builder().code(code).name(name).isInEurope(isInEurope).build();

    val actual = dto.toCountry();

    assertThat(actual)
        .isEqualTo(Country.builder().code(code).name(name).inEurope(isInEurope).build());
  }
}
