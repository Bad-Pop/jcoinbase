package com.github.badpop.jcoinbase.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.service.dto.*;
import io.vavr.API;
import io.vavr.collection.Seq;

import java.net.http.HttpResponse;

/**
 * Utility interface that allow us to deserialize coinbase api responses from json to java objects
 */
public interface JsonDeserializationService {

  /**
   * Generic method to centralized the deserialization process of coinbase api responses and wrap
   * the results in a {@link CallResult} object
   *
   * <p>This method is different of {@link #singleFailureDeserialize(HttpResponse, ObjectMapper,
   * TypeReference)} and should be used for all Coinbase api responses except for public data.
   * Public data does not return errors in the same way as protected data.
   *
   * @param response the http response
   * @param jsonSerDes the jackson object mapper to use
   * @param typeReference the jackson type reference for deserialization
   * @param <T> the object type to deserialize
   * @return a new {@link CallResult} representing a success or a failure
   * @throws JsonProcessingException if not deserializable
   */
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

  static <T> CallResult<Seq<CoinbaseError>, PaginatedResponseDto<T>> paginatedDeserialize(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<PaginatedResponseDto<T>> typeReference)
      throws JsonProcessingException {
    if (isOk(response.statusCode())) {
      return buildPaginatedSuccess(response, jsonSerDes, typeReference);
    } else {
      return buildPaginatedFailure(response, jsonSerDes);
    }
  }

  /**
   * Generic method to centralized the deserialization process of coinbase api responses and wrap
   * the results in a {@link CallResult} object
   *
   * <p>This method is different of {@link #deserialize(HttpResponse, ObjectMapper, TypeReference)}
   * and should be used only for public data. Protected data does not return errors in the same way
   * as public data.
   *
   * @param response the http response
   * @param jsonSerDes the jackson object mapper to use
   * @param typeReference the jackson type reference for deserialization
   * @param <T> the object type to deserialize
   * @return a new {@link CallResult} representing a success or a failure
   * @throws JsonProcessingException if not deserializable
   */
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

  /**
   * A simple method to check if a response status is between 200 and 204
   *
   * @param statusCode the http response status code
   * @return true if between 200 and 204, false otherwise
   */
  private static boolean isOk(final int statusCode) {
    return statusCode >= 200 && statusCode <= 204;
  }

  /**
   * Create a new {@link com.github.badpop.jcoinbase.control.CallResult.Success} containing the
   * result
   *
   * @param response the http response
   * @param jsonSerDes the jackson object mapper to use
   * @param typeReference the jackson type reference for deserialization
   * @param <T> the object type to deserialize
   * @return a new {@link CallResult} representing a success
   * @throws JsonProcessingException if not deserializable
   */
  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildSuccess(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference)
      throws JsonProcessingException {
    return CallResult.success(jsonSerDes.readValue(response.body(), typeReference));
  }

  private static <T> CallResult<Seq<CoinbaseError>, PaginatedResponseDto<T>> buildPaginatedSuccess(
      final HttpResponse<String> response,
      final ObjectMapper jsonSerDes,
      final TypeReference<PaginatedResponseDto<T>> typeReference)
      throws JsonProcessingException {
    return CallResult.success(jsonSerDes.readValue(response.body(), typeReference));
  }

  /**
   * Create a new {@link com.github.badpop.jcoinbase.control.CallResult.Failure} containing the
   * error(s). Use only for coinbase protected data.
   *
   * @param response the http response
   * @param jsonSerDes the jackson object mapper to use
   * @param <T> the object type to deserialize
   * @return a new {@link CallResult} representing a failure
   * @throws JsonProcessingException if not deserializable
   */
  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildFailure(
      final HttpResponse<String> response, final ObjectMapper jsonSerDes)
      throws JsonProcessingException {
    return CallResult.failure(
        jsonSerDes
            .readValue(response.body(), new TypeReference<DataErrorsDto>() {})
            .toCoinbaseErrors());
  }

  private static <T> CallResult<Seq<CoinbaseError>, PaginatedResponseDto<T>> buildPaginatedFailure(
      final HttpResponse<String> response, final ObjectMapper jsonSerDes)
      throws JsonProcessingException {
    return CallResult.failure(
        jsonSerDes
            .readValue(response.body(), new TypeReference<DataErrorsDto>() {})
            .toCoinbaseErrors());
  }

  /**
   * Create a new {@link com.github.badpop.jcoinbase.control.CallResult.Failure} containing a single
   * error. Only for coinbase public data.
   *
   * @param response the http response
   * @param jsonSerDes the jackson object mapper to use
   * @param <T> the object type to deserialize
   * @return a new {@link CallResult} representing a failure
   * @throws JsonProcessingException if not deserializable
   */
  private static <T> CallResult<Seq<CoinbaseError>, DataDto<T>> buildSingleFailure(
      final HttpResponse<String> response, final ObjectMapper jsonSerDes)
      throws JsonProcessingException {
    return CallResult.failure(
        API.Seq(
            jsonSerDes
                .readValue(response.body(), new TypeReference<DataErrorDto<SingleErrorDto>>() {})
                .getError()
                .toCoinbaseError()));
  }
}
