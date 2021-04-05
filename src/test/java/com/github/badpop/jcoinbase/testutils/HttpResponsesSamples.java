package com.github.badpop.jcoinbase.testutils;

import lombok.SneakyThrows;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public abstract class HttpResponsesSamples {

  public static final HttpResponse<String> CURRENT_USER_HTTP_RESPONSE_OK =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 200;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/coinbaseUserService/current_user.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  public static final HttpResponse<String> CURRENT_USER_HTTP_RESPONSE_KO =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 400;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/errors.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  public static final HttpResponse<String> TIME_HTTP_RESPONSE_OK =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 200;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/coinbaseDataService/time.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  public static final HttpResponse<String> TIME_HTTP_RESPONSE_KO =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 400;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/error.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  public static final HttpResponse<String> ACCOUNTS_HTTP_RESPONSE_OK =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 200;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/coinbaseAccountService/account_list.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };

  public static final HttpResponse<String> ACCOUNTS_HTTP_RESPONSE_KO =
      new HttpResponse<>() {
        @Override
        public int statusCode() {
          return 400;
        }

        @Override
        public HttpRequest request() {
          return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
          return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
          return null;
        }

        @SneakyThrows
        @Override
        public String body() {
          return JsonUtils.readResource("/json/errors.json");
        }

        @Override
        public Optional<SSLSession> sslSession() {
          return Optional.empty();
        }

        @Override
        public URI uri() {
          return null;
        }

        @Override
        public HttpClient.Version version() {
          return null;
        }
      };
}
