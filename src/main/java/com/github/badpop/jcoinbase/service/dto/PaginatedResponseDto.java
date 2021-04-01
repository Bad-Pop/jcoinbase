package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.service.WarningManagerService;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static io.vavr.API.Option;

@Getter
@AllArgsConstructor
public class PaginatedResponseDto<T> {
  private final PaginationDto pagination;
  private final Seq<T> data;
  private final Seq<WarningDto> warnings;

  // TODO TEST
  public <U> PaginatedResponse<U> toPaginatedResponse(Seq<U> data) {
    WarningManagerService.alertIfCoinbaseHasReturnedWarnings(this);
    return PaginatedResponse.<U>builder()
        .pagination(Option(pagination).map(PaginationDto::toPagination).getOrNull())
        .data(data)
        .build();
  }
}
