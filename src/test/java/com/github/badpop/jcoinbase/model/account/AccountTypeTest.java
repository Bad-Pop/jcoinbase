package com.github.badpop.jcoinbase.model.account;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.badpop.jcoinbase.model.account.AccountType.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class AccountTypeTest {

  @Nested
  class FromString {
    @Test
    void should_retrieve_type() {
      val wallet = "WaLlEt";
      val fiat = "FiAt";
      val vault = "VaUlT";

      val actualWallet = AccountType.fromString(wallet);
      val actualFiat = AccountType.fromString(fiat);
      val actualVault = AccountType.fromString(vault);

      assertThat(actualWallet).contains(WALLET);
      assertThat(actualFiat).contains(FIAT);
      assertThat(actualVault).contains(VAULT);
    }

    @Test
      void should_not_retrieve_type(){
        val unknown = "lorem ipsum";
        val actual = AccountType.fromString(unknown);
        assertThat(actual).isEmpty();
    }
  }

  @Nested
  class FromStringAsJava {
      @Test
      void should_retrieve_type() {
          val wallet = "WaLlEt";
          val fiat = "FiAt";
          val vault = "VaUlT";

          val actualWallet = AccountType.fromStringAsJava(wallet);
          val actualFiat = AccountType.fromStringAsJava(fiat);
          val actualVault = AccountType.fromStringAsJava(vault);

          Assertions.assertThat(actualWallet).contains(WALLET);
          Assertions.assertThat(actualFiat).contains(FIAT);
          Assertions.assertThat(actualVault).contains(VAULT);
      }

      @Test
      void should_not_retrieve_type(){
          val unknown = "lorem ipsum";
          val actual = AccountType.fromStringAsJava(unknown);
          Assertions.assertThat(actual).isEmpty();
      }
  }
}
