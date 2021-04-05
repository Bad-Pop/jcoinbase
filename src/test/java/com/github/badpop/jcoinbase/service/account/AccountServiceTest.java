package com.github.badpop.jcoinbase.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.badpop.jcoinbase.JCoinbaseClient;
import com.github.badpop.jcoinbase.JCoinbaseProperties;
import com.github.badpop.jcoinbase.control.CallResult;
import com.github.badpop.jcoinbase.exception.InvalidRequestException;
import com.github.badpop.jcoinbase.exception.JCoinbaseException;
import com.github.badpop.jcoinbase.exception.NoNextPageException;
import com.github.badpop.jcoinbase.exception.NoPreviousPageException;
import com.github.badpop.jcoinbase.model.CoinbaseError;
import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.Pagination;
import com.github.badpop.jcoinbase.model.account.Account;
import com.github.badpop.jcoinbase.model.account.AccountsPage;
import com.github.badpop.jcoinbase.model.request.UpdateAccountRequest;
import com.github.badpop.jcoinbase.service.auth.AuthenticationService;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.badpop.jcoinbase.testutils.JsonSerDesSample.JSON_SER_DES;
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
  class GetAccountsPage {
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
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
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }
  }

  @Nested
  class GetNextAccountsPage {
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
      val pagination = Pagination.builder().nextUri("").build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getNextAccountsPageAsJava(pagination))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_accounts() {
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
      val pagination = Pagination.builder().nextUri("").build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getNextAccountsPage(pagination))
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
      val pagination = Pagination.builder().build();
      assertThatExceptionOfType(NoNextPageException.class)
          .isThrownBy(() -> accountService.getNextAccountsPageAsJava(pagination));
    }

    @Test
    void should_throws_NoNextPageException() {
      val pagination = Pagination.builder().build();
      assertThatExceptionOfType(NoNextPageException.class)
          .isThrownBy(() -> accountService.getNextAccountsPage(pagination));
    }
  }

  @Nested
  class GetPreviousAccountsPage {
    @Test
    void should_return_as_java() {
      val paginatedResponse =
          PaginatedResponse.<Account>builder()
              .pagination(Pagination.builder().previousUri("prevUri").build())
              .build();

      when(coinbaseAccountService.fetchAccountPageByUri(
              client, authenticationService, paginatedResponse.getPagination().getPreviousUri()))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual = accountService.getPreviousAccountsPageAsJava(paginatedResponse.getPagination());

      assertThat(actual)
          .usingRecursiveComparison()
          .isEqualTo(
              CallResult.success(
                  new AccountsPage(
                      paginatedResponse.getPagination(), paginatedResponse.getData())));

      verifyNoInteractions(client);
      verify(coinbaseAccountService)
          .fetchAccountPageByUri(
              client, authenticationService, paginatedResponse.getPagination().getPreviousUri());
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");
      val pagination = Pagination.builder().previousUri("").build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getPreviousAccountsPageAsJava(pagination))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_return_accounts() {
      val paginatedResponse = PaginatedResponse.<Account>builder().build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(success(CallResult.success(paginatedResponse)));

      val actual =
          accountService.getPreviousAccountsPage(Pagination.builder().previousUri("").build());

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
      val pagination = Pagination.builder().previousUri("").build();

      when(coinbaseAccountService.fetchAccountPageByUri(client, authenticationService, ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getPreviousAccountsPage(pagination))
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
          accountService.getPreviousAccountsPageAsJava(
              Pagination.builder().previousUri("").build());

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

      val actual =
          accountService.getPreviousAccountsPage(Pagination.builder().previousUri("").build());

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verifyNoInteractions(client);
      verify(coinbaseAccountService).fetchAccountPageByUri(client, authenticationService, "");
      verifyNoMoreInteractions(coinbaseAccountService);
    }

    @Test
    void should_throws_NoPreviousPageException_when_as_java() {
      val pagination = Pagination.builder().build();
      assertThatExceptionOfType(NoPreviousPageException.class)
          .isThrownBy(() -> accountService.getPreviousAccountsPageAsJava(pagination));
    }

    @Test
    void should_throws_NoPreviousPageException() {
      val pagination = Pagination.builder().build();
      assertThatExceptionOfType(NoPreviousPageException.class)
          .isThrownBy(() -> accountService.getPreviousAccountsPage(pagination));
    }
  }

  @Nested
  class GetAccount {
    @Test
    void should_throws_InvalidRequestException_if_id_is_blank() {
      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccount(null));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccount(""));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccount(" "));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccountAsJava(null));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccountAsJava(""));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.getAccountAsJava(" "));
    }

    @Test
    void should_return_as_java() {
      val account = Account.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(success(CallResult.success(account)));

      val actual = accountService.getAccountAsJava("id");

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty().contains(account);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java() {
      val jcex = new JCoinbaseException("error message");

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getAccountAsJava("id"))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return() {
      val account = Account.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(success(CallResult.success(account)));

      val actual = accountService.getAccount("id");

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty().contains(account);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure() {
      val jcex = new JCoinbaseException("error message");

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.getAccount("id"))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return_CallResult_failure_as_java() {
      val error = CoinbaseError.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getAccountAsJava("id");

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return_CallResult_failure() {
      val error = CoinbaseError.builder().build();

      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(client, authenticationService, "/v2/accounts/id", "GET", ""))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.getAccount("id");

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "GET", "");
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }
  }

  @Nested
  class UpdateAccount {
    @Test
    void should_throws_InvalidRequestException_if_id_is_blank() {

      val request = UpdateAccountRequest.builder().build();

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccount(null, request));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccount("", request));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccount(" ", request));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccountAsJava(null, request));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccountAsJava("", request));

      assertThatExceptionOfType(InvalidRequestException.class)
          .isThrownBy(() -> accountService.updateAccountAsJava(" ", request));
    }

    @Test
    void should_throws_NPE_if_request_is_null() {
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> accountService.updateAccount("id", null));

      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> accountService.updateAccountAsJava("id", null));
    }

    @Test
    void should_return_as_java() throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val account = Account.builder().build();

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(success(CallResult.success(account)));

      val actual = accountService.updateAccountAsJava("id", request);

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty().contains(account);

      verify(client).getProperties();
      verify(client).getJsonSerDes();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure_as_java()
        throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val jcex = new JCoinbaseException("error message");

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.updateAccountAsJava("id", request))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(client).getJsonSerDes();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return() throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val account = Account.builder().build();

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(success(CallResult.success(account)));

      val actual = accountService.updateAccount("id", request);

      assertThat(actual).isInstanceOf(CallResult.class).isNotEmpty().contains(account);

      verify(client).getProperties();
      verify(client).getJsonSerDes();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_throws_JCoinbaseException_if_CoinbaseAccountService_return_a_failure()
        throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val jcex = new JCoinbaseException("error message");

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(failure(jcex));

      assertThatExceptionOfType(JCoinbaseException.class)
          .isThrownBy(() -> accountService.updateAccount("id", request))
          .withMessage("com.github.badpop.jcoinbase.exception.JCoinbaseException: error message");

      verify(client).getProperties();
      verify(client).getJsonSerDes();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return_CallResult_failure_as_java() throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val error = CoinbaseError.builder().build();

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.updateAccountAsJava("id", request);

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(java.util.List.of(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getJsonSerDes();
      verify(client).getProperties();
      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }

    @Test
    void should_return_CallResult_failure() throws JsonProcessingException {
      val request = UpdateAccountRequest.builder().build();
      val body = JSON_SER_DES.writeValueAsString(request);
      val error = CoinbaseError.builder().build();

      when(client.getJsonSerDes()).thenReturn(JSON_SER_DES);
      when(client.getProperties()).thenReturn(properties);
      when(properties.getAccountsPath()).thenReturn("/v2/accounts");
      when(coinbaseAccountService.send(
              client, authenticationService, "/v2/accounts/id", "PUT", body))
          .thenReturn(success(CallResult.failure(Seq(error))));

      val actual = accountService.updateAccount("id", request);

      assertThat(actual.isFailure()).isTrue();
      assertThat(actual).isEqualTo(CallResult.failure(Seq(error)));
      assertThat(actual.getFailure()).containsExactly(error);

      verify(client).getProperties();

      verify(properties).getAccountsPath();
      verify(coinbaseAccountService)
          .send(client, authenticationService, "/v2/accounts/id", "PUT", body);
      verifyNoMoreInteractions(coinbaseAccountService, client, properties);
    }
  }
}
