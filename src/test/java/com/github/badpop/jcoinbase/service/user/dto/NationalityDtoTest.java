package com.github.badpop.jcoinbase.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Nationality;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NationalityDtoTest {

  @Test
  void should_map() {
    val code = "code";
    val name = "name";
    val dto = NationalityDto.builder().code(code).name(name).build();

    val actual = dto.toNationality();

    assertThat(actual).isEqualTo(Nationality.builder().code(code).name(name).build());
  }
}
