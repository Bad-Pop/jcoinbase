package com.github.badpop.jcoinbase.service.dto;

import com.github.badpop.jcoinbase.model.Pagination;
import com.github.badpop.jcoinbase.model.Pagination.Order;
import lombok.AllArgsConstructor;

import static com.github.badpop.jcoinbase.model.Pagination.Order.DESC;

@AllArgsConstructor
public class PaginationDto {
  private final int limit;
  private final String order;
  private final String endingBefore;
  private final String startingAfter;
  private final String previousEndingBefore;
  private final String nextStartingAfter;
  private final String previousUri;
  private final String nextUri;

  public Pagination toPagination() {
    return Pagination.builder()
        .limit(limit)
        .order(Order.fromString(order).getOrElse(DESC))
        .endingBefore(endingBefore)
        .startingAfter(startingAfter)
        .previousEndingBefore(previousEndingBefore)
        .nextStartingAfter(nextStartingAfter)
        .previousUri(previousUri)
        .nextUri(nextUri)
        .build();
  }
}
