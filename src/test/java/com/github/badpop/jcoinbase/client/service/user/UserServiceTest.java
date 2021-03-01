package com.github.badpop.jcoinbase.client.service.user;

import com.github.badpop.jcoinbase.client.JCoinbaseClient;
import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.client.service.auth.AuthenticationService;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @Test
  void should_return_current_user() {
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
  void should_throw_exception_when_fetch_is_failure() {
    var ex = new JCoinbaseException("not allowed");

    when(coinbaseUserService.fetchCurrentUser(client, authenticationService)).thenThrow(ex);

    assertThatExceptionOfType(JCoinbaseException.class)
        .isThrownBy(() -> userService.fetchCurrentUser())
        .isEqualTo(ex);
  }

  @Test
  void main() {

    var client =
        JCoinbaseClientFactory.build("W4QSrWYqgm8cCIPO", "GEpjPp44AoO7HKlgK6MDJcG0Pawn1FOC");

    var result = client.user().fetchCurrentUser();

    System.out.println(result);
  }
}
