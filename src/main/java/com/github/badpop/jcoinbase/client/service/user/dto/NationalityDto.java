package com.github.badpop.jcoinbase.client.service.user.dto;

import com.github.badpop.jcoinbase.model.user.Nationality;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NationalityDto {
  private final String code;
  private final String name;

  public Nationality toNationality() {
    return Nationality.builder().name(name).code(code).build();
  }
}
