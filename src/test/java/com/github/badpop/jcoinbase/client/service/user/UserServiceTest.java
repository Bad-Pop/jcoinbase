package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.user.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.vavr.API.Seq;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks private UserService userService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseUserService coinbaseUserService;
  @Mock private AuthenticationService authenticationService;

  @Nested
  class CurrentUser {
    @Test
    void fetchCurrentUserAsJava_should_return_current_user() {
      val user = User.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.fetchCurrentUserAsJava();

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void
        fetchCurrentUserAsJava_should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.fetchCurrentUserAsJava())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void fetchCurrentUser_should_return_current_user() {
      val user = User.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.fetchCurrentUser();

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void
        fetchCurrentUser_should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.fetchCurrentUser())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void fetchCurrentUserAsJava_should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.fetchCurrentUserAsJava();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void fetchCurrentUser_should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.fetchCurrentUser();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }
  }
}
