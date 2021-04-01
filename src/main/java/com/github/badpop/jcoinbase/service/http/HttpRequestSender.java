package com.github.badpop.jcoinbase.service.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.service.WarningManagerService;
import com.github.badpop.jcoinbase.service.dto.DataDto;
import com.github.badpop.jcoinbase.service.dto.PaginatedResponseDto;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import static com.github.badpop.jcoinbase.service.http.JsonDeserializationService.*;

public interface HttpRequestSender {

  static <T> Try<CallResult<Seq<CoinbaseError>, DataDto<T>>> send(
      final HttpClient httpClient,
      final HttpRequest request,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference) {

    return Try.of(() -> httpClient.send(request, BodyHandlers.ofString()))
        .mapTry(
            response ->
                deserialize(response, jsonSerDes, typeReference)
                    .peek(WarningManagerService::alertIfCoinbaseHasReturnedWarnings));
  }

  // TODO TEST
  static <T> Try<CallResult<Seq<CoinbaseError>, PaginatedResponseDto<T>>> paginatedSend(
      final HttpClient httpClient,
      final HttpRequest request,
      final ObjectMapper jsonSerDes,
      final TypeReference<PaginatedResponseDto<T>> typeReference) {

    return Try.of(() -> httpClient.send(request, BodyHandlers.ofString()))
        .mapTry(response -> paginatedDeserialize(response, jsonSerDes, typeReference));
  }

  static <T> Try<CallResult<Seq<CoinbaseError>, DataDto<T>>> singleFailureSend(
      final HttpClient httpClient,
      final HttpRequest request,
      final ObjectMapper jsonSerDes,
      final TypeReference<DataDto<T>> typeReference) {

    return Try.of(() -> httpClient.send(request, BodyHandlers.ofString()))
        .mapTry(
            response ->
                singleFailureDeserialize(response, jsonSerDes, typeReference)
                    .peek(WarningManagerService::alertIfCoinbaseHasReturnedWarnings));
  }
}
