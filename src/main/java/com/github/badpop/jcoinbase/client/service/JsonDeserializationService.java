package com.github.badpop.jcoinbase.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.jcoinbase.client.service.dto.DataDto;
import com.github.badpop.jcoinbase.client.service.dto.DataErrorDto;
import com.github.badpop.jcoinbase.client.service.dto.DataErrorsDto;
import com.github.badpop.jcoinbase.client.service.dto.SingleErrorDto;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import io.vavr.collection.Seq;

import java.net.http.HttpResponse;

import static io.vavr.API.Seq;

public interface JsonDeserializationService {

  static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> deserialize(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    if (isOk(response.statusCode())) {
      return buildSuccess(response, jsonSerDes, typeReference);
    } else {
      return buildFailure(response, jsonSerDes);
    }
  }

  // TODO REFACTOR TO HAVE ONLY ONE DESERIALIZATION METHOD
  static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> singleFailureDeserialize(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    if (isOk(response.statusCode())) {
      return buildSuccess(response, jsonSerDes, typeReference);
    } else {
      return buildSingleFailure(response, jsonSerDes);
    }
  }

  private static boolean isOk(final int statusCode) {
    return statusCode >= 200 && statusCode <= 204;
  }

  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildSuccess(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    return CallResult.success(jsonSerDes.readValue(response.body(), typeReference));
  }

  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildFailure(
      final HttpResponse<String> response, final ObjectMapper jsonSerDes)
      throws JsonProcessingException {
    return CallResult.failure(
        jsonSerDes
            .readValue(response.body(), new TypeReference<DataErrorsDto>() {})
            .toCoinbaseErrors());
  }

  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildSingleFailure(
      final HttpResponse<String> response, final ObjectMapper jsonSerDes)
      throws JsonProcessingException {
    return CallResult.failure(
        Seq(
            jsonSerDes
                .readValue(response.body(), new TypeReference<DataErrorDto<SingleErrorDto>>() {})
                .getError()
                .toCoinbaseError()));
  }
}
