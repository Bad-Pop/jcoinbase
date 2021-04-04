package com.github.badpop.jcoinbase.service.account;

import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.JCoinbaseProperties;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.exception.NoNextPageException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.Pagination;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountsPage;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
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
  @Mock private JCoinbaseProperties properties;
  @Mock private CoinbaseAccountService coinbaseAccountService;
  @Mock private AuthenticationService authenticationService;

  @Nested
  class GetPaginatedAccountsList {
    @Test
    void should_return_as_java() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getAccountsPageAsJava();

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getAccountsPageAsJava())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_acounts() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getAccountsPage();

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getAccountsPage())
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getAccountsPageAsJava();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("");
      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getAccountsPage();

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }
  }

  @Nested
  class GetNextPaginatedAccountsList {
    @Test
    void should_return_as_java() {
      val paginatedResponse =
          PaginatedResponse.<Account>builder()
              .pagination(Pagination.builder().nextUri("nextUri").build())
              .build();

      when(coinbaseAccountService.fetchAccountPageByUri(
              client, authenticationService, paginatedResponse.getPagination().getNextUri()))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getNextAccountsPageAsJava(paginatedResponse.getPagination());

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verifyNoInteractions(client);
      verify(coinbaseAccountService)
          .fetchAccountPageByUri(
              client, authenticationService, paginatedResponse.getPagination().getNextUri());
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(
              () ->
                  accountService.getNextAccountsPageAsJava(
                      Pagination.builder().nextUri("").build()))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_acounts() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getNextAccountsPage(Pagination.builder().nextUri("").build());

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(
              () -> accountService.getNextAccountsPage(Pagination.builder().nextUri("").build()))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual =
          accountService.getNextAccountsPageAsJava(Pagination.builder().nextUri("").build());

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getNextAccountsPage(Pagination.builder().nextUri("").build());

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_NoNextPageException_when_as_java() {
      assertThatExceptionOfType(NoNextPageException.class)
          .isThrownBy(() -> accountService.getNextAccountsPageAsJava(Pagination.builder().build()));
    }

    @Test
    void should_throws_NoNextPageException() {
      assertThatExceptionOfType(NoNextPageException.class)
          .isThrownBy(() -> accountService.getNextAccountsPage(Pagination.builder().build()));
    }
  }
}
