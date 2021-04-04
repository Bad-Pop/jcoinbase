package com.github.badpop.jcoinbase.model;

import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@ToString
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class PaginatedResponse<T> {
  Pagination pagination;
  Seq<T> data;

  public List<T> getDataAsJava() {
    return data.asJava();
  }
}
