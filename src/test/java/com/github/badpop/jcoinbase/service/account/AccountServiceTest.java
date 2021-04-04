package com.github.badpop.jcoinbase.service.account;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountsPage;
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
class AccountServiceTest {

  @InjectMocks private AccountService accountService;
  @Mock private JCoinbaseClient client;
  @Mock private CoinbaseAccountService coinbaseAccountService;
  @Mock private AuthenticationService authenticationService;

  @Nested
  class GetPaginatedAccountsList {
    @Test
    void should_return_as_java() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getPaginatedAccountsListAsJava();

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getPaginatedAccountsListAsJava())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_acounts() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getPaginatedAccountsList();

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getPaginatedAccountsList())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getPaginatedAccountsListAsJava();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseAccountService.fetchAccountsList(client, authenticationService))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getPaginatedAccountsList();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountsList(client, authenticationService);
      verifyNoMoreInteractions(coinbaseAccountService);
    }
  }
}
