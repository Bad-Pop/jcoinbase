package com.github.badpop.jcoinbase.model;

import io.vavr.collection.Seq;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
public class PaginatedResponse<T> {
  Pagination pagination;
  @With Seq<T> data;

  // TODO TEST
  public List<T> getDataAsJava() {
    return data.asJava();
  }
}
