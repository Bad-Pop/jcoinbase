package com.github.badpop.jcoinbase.model.account;

import com.github.badpop.jcoinbase.model.Pagination;
import io.vavr.collection.Seq;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AccountsListResponse {
  Pagination pagination;
  Seq<Account> accounts;

  public List<Account> getAccountsAsJava() {
    return accounts.toJavaList();
  }
}
