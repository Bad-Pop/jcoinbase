package com.github.badpop.jcoinbase;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

@Slf4j
@Disabled("For development purpose only")
@DisplayName("Test class for development purpose only")
public class DevelopmentTest {

  //  @Test
  //  @DisplayName("Test case for development purpose only")
  //  void main() {
  //
  //    val client =
  //        JCoinbaseClientFactory.build(
  //            "B4FyyXIxMbtAlAfe",
  //            "34ltm3h8KBzFC66YqWXhYfp4RVM80loQ",
  //            3,
  //            false,
  //            false,
  //            ZoneId.of("UTC+01:00")); // PARIS ZONE ID
  //
  //    val data = client.user().fetchCurrentUser();
  //
  //    log.info("Data : {}", data);
  //
  //    log.info("get : {}", callSuccess().get());
  //    log.info("getOrElse Failure: {}", callFailure().getOrElse(User.builder().build()));
  //    log.info("getOrElse Success: {}", callSuccess().getOrElse(User.builder().build()));
  //    log.info("getOrElseGet Failure: {}", callFailure().getOrElseGet(() ->
  // User.builder().build()));
  //    log.info("getOrElseGet Success: {}", callSuccess().getOrElseGet(() ->
  // User.builder().build()));
  //    log.info("getErrors : {}", callFailure().getErrors());
  //    callSuccess().peek(user -> log.info("peek : {}", user));
  //    callFailure().onError(() -> log.error("onError -> error :)"));
  //    callFailure().getOrElseThrow(() -> new JCoinbaseException("Unknown internal error"));
  //  }

  //  private CallResult<User> callSuccess() {
  //    return CallResult.<User>builder().result(Option.of(User.builder().build())).build();
  //  }
  //
  //  private CallResult<User> callFailure() {
  //    return CallResult.<User>builder()
  //        .errors(Option.of(Seq(CoinbaseError.builder().build())))
  //        .build();
  //  }

  //  @Test
  //  @DisplayName("Mappers case for development purpose only")
  //  void mappers() {
  //    val user = User.builder().build();
  //    val userCallResultSuccess = CallResultMapper.mapSuccess(user);
  //    assertThat(userCallResultSuccess.isSuccess()).isTrue();
  //    log.info("user success : {}", userCallResultSuccess);
  //
  //    val errors = Seq(new ErrorDto("code", "message", null));
  //    val userCallResultFailure = CallResultMapper.<User>mapFailure(errors);
  //    assertThat(userCallResultFailure.isFailure()).isTrue();
  //    assertThat(userCallResultFailure.getErrors()).hasSize(1);
  //    log.info("user failure : {}", userCallResultFailure);
  //  }

  //  @Test
  //  @DisplayName("CallResult case for development purpose only")
  //  void callResultRealWorld() {
  //    val client =
  //        JCoinbaseClientFactory.buildWithoutThreadSafeSingleton(
  //            "B4FyyXIxMbtAlAfe",
  //            "34ltm3h8KBzFC66YqWXhYfp4RVM80loQ",
  //            0,
  //            false,
  //            ZoneId.of("UTC+01:00"));
  //
  //    val result =
  //        client
  //            .user()
  //            .fetchCurrentUserTest()
  //            .getOrElseGet(userCallResult -> CallResult.<User>builder().build());
  //
  //    log.info("result : {}", result);
  //  }

  //  @Test
  //  @DisplayName("CallResult API case for development purpose only")
  //  void callResultApi() {
  //
  //    callSuccessApi().forEach(user -> log.info("forEach : {}", user));
  //
  //    assertThatExceptionOfType(JCoinbaseException.class)
  //        .isThrownBy(() -> callFailureApi().getOrElseThrow(() -> new
  // JCoinbaseException("message")));
  //
  //    log.info("getOrElseTry : {}", callFailureApi().getOrElseTry(() -> User.builder().build()));
  //
  //    val toto = com.github.badpop.jcoinbase.api.CallResult.narrow(callFailureApi());
  //
  //    assertThat(callSuccessApi().isSuccess()).isTrue();
  //  }
  //
  //  private com.github.badpop.jcoinbase.api.CallResult<Seq<CoinbaseError>, User> callSuccessApi()
  // {
  //    return com.github.badpop.jcoinbase.api.CallResult.success(User.builder().build());
  //  }
  //
  //  private com.github.badpop.jcoinbase.api.CallResult<Seq<CoinbaseError>, User> callFailureApi()
  // {
  //    return
  // com.github.badpop.jcoinbase.api.CallResult.failure(Seq(CoinbaseError.builder().build()));
  //  }
}
