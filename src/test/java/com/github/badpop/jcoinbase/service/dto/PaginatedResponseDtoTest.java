package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.service.data.dto.TimeDto;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;

class PaginatedResponseDtoTest {

  @Test
  void should_map_to_paginatedResponse() {
    val pagination = new PaginationDto(1, "asc", "", "", "", "", "", "");
    val time = new TimeDto(Instant.ofEpochMilli(129235273), 129235273);
    val dto = new PaginatedResponseDto<>(pagination, Seq(time), Seq());

    val actual = dto.toPaginatedResponse(Seq(time.toTime()));

    assertThat(actual)
        .isInstanceOf(PaginatedResponse.class)
        .usingRecursiveComparison()
        .isEqualTo(
            PaginatedResponse.builder()
                .pagination(pagination.toPagination())
                .data(Seq(time.toTime()))
                .build());
  }
}
