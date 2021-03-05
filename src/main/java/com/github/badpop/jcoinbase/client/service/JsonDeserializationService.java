package com.github.badpop.jcoinbase.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import com.github.badpop.jcoinbase.client.service.dto.ErrorDto;
import com.github.badpop.jcoinbase.client.service.dto.ErrorsDto;
import com.github.badpop.jcoinbase.client.service.dto.mapper.CoinbaseErrorMapper;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import io.vavr.collection.Seq;

import java.net.http.HttpResponse;

import static io.vavr.API.Seq;

public interface JsonDeserializationService {

  // TODO TEST
  static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> deserialize(
      final HttpResponse<String> response,
      final ObjectMapper mapper,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    if (response.statusCode() >= 200 && response.statusCode() <= 204) {
      return CallResult.success(mapper.readValue(response.body(), typeReference));
    } else {
      return CallResult.failure(
          CoinbaseErrorMapper.map(
              mapper.readValue(response.body(), new TypeReference<ErrorsDto>() {}).getErrors()));
    }
  }

  // TODO TEST
  // TODO CHECK IF IT WORKS WITH SINGLE ERROR CASE
  static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> deserializeSingleError(
      final HttpResponse<String> response,
      final ObjectMapper mapper,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    if (response.statusCode() >= 200 && response.statusCode() <= 204) {
      return CallResult.success(mapper.readValue(response.body(), typeReference));
    } else {
      return CallResult.failure(
          Seq(
              CoinbaseErrorMapper.map(
                  mapper.readValue(response.body(), new TypeReference<ErrorDto>() {}))));
    }
  }
}
