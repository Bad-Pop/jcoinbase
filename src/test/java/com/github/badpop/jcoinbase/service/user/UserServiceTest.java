package com.github.badpop.jcoinbase.service.user;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.request.UpdateCurrentUserRequest;
import com.github.badpop.jcoinbase.model.user.Authorizations;
import com.github.badpop.jcoinbase.model.user.User;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks private UserService userService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseUserService coinbaseUserService;
  @Mock private AuthenticationService authenticationService;

  @Nested
  class GetCurrentUser {
    @Test
    void should_return_current_user_as_java() {
      val user = User.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.getCurrentUserAsJava();

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getCurrentUserAsJava())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_current_user() {
      val user = User.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.getCurrentUser();

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getCurrentUser())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getCurrentUserAsJava();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getCurrentUser();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }
  }

  @Nested
  class GetAuthorization {
    @Test
    void should_return_current_user_auths_as_java() {
      val auths = Authorizations.builder().build();

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(success(CallResult.success(auths)));

      val actual = userService.getAuthorizationsAsJava();

      assertThat(actual).isEqualTo(CallResult.success(auths));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getAuthorizationsAsJava())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_current_user_auths() {
      val auths = Authorizations.builder().build();

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(success(CallResult.success(auths)));

      val actual = userService.getAuthorizations();

      assertThat(actual).isEqualTo(CallResult.success(auths));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getAuthorizations())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getAuthorizationsAsJava();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getAuthorizations();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }
  }

  @Nested
  class GetUserById {
    @Test
    void should_return_user_as_java() {
      val userId = "id";
      val user = User.builder().build();

      when(coinbaseUserService.fetchUserById(client, authenticationService, userId))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.getUserByIdAsJava(userId);

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchUserById(client, authenticationService, userId);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure_as_java() {
      val userId = "id";
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchUserById(client, authenticationService, userId))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getUserByIdAsJava(userId))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchUserById(client, authenticationService, userId);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_user() {
      val userId = "id";
      val user = User.builder().build();

      when(coinbaseUserService.fetchUserById(client, authenticationService, userId))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.getUserById(userId);

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchUserById(client, authenticationService, userId);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val userId = "id";
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.fetchUserById(client, authenticationService, userId))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.getUserById(userId))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchUserById(client, authenticationService, userId);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val userId = "id";
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchUserById(client, authenticationService, userId))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getUserByIdAsJava(userId);

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchUserById(client, authenticationService, userId);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.fetchAuthorizations(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.getAuthorizations();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).fetchAuthorizations(client, authenticationService);
      verifyNoMoreInteractions(coinbaseUserService);
    }
  }

  @Nested
  class UpdateCurrentUser {
    @Test
    void should_return_user_as_java() {
      val request = UpdateCurrentUserRequest.builder().build();
      val user = User.builder().build();

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.updateCurrentUserAsJava(request);

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure_as_java() {
      val request = UpdateCurrentUserRequest.builder().build();
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.updateCurrentUserAsJava(request))
          .withMessage("An error occurred while updating the current user")
          .withCause(jcex);

      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_user() {
      val request = UpdateCurrentUserRequest.builder().build();
      val user = User.builder().build();

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(success(CallResult.success(user)));

      val actual = userService.updateCurrentUser(request);

      assertThat(actual).isEqualTo(CallResult.success(user));
      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
      val request = UpdateCurrentUserRequest.builder().build();
      val jcex = new JCoinbaseException("error message");

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> userService.updateCurrentUser(request))
          .withMessage("An error occurred while updating the current user")
          .withCause(jcex);

      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val request = UpdateCurrentUserRequest.builder().build();
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.updateCurrentUserAsJava(request);

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }

    @Test
    void should_return_CallResult_failure() {
      val request = UpdateCurrentUserRequest.builder().build();
      val error = CoinbaseError.builder().build();

      when(coinbaseUserService.updateCurrentUser(client, authenticationService, request))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = userService.updateCurrentUser(request);

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseUserService).updateCurrentUser(client, authenticationService, request);
      verifyNoMoreInteractions(coinbaseUserService);
    }
  }
}
