package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @Test
  void fetchCurrentUser_should_return_current_user() {
    var user = User.builder().build();

    when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
        .thenReturn(success(user));

    var actual = userService.fetchCurrentUser();

    assertThat(actual).isEqualTo(user);
    verifyNoInteractions(client);
    verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
    verifyNoMoreInteractions(coinbaseUserService);
  }

  @Test
  void fetchCurrentUser_should_throws_JCoinbaseException_if_CoinbaseUserService_return_a_failure() {
    var jcex = new JCoinbaseException("error message");

    when(coinbaseUserService.fetchCurrentUser(client, authenticationService))
        .thenReturn(failure(jcex));

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> userService.fetchCurrentUser())
        .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

    verifyNoInteractions(client);
    verify(coinbaseUserService).fetchCurrentUser(client, authenticationService);
    verifyNoMoreInteractions(coinbaseUserService);
  }
}
