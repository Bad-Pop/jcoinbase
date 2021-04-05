package com.github.badpop.jcoinbase.model.account;

import com.github.badpop.jcoinbase.model.PaginatedResponse;
import com.github.badpop.jcoinbase.model.Pagination;
import io.vavr.collection.Seq;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AccountsPage extends PaginatedResponse<Account> {

  public AccountsPage(Pagination pagination, Seq<Account> data) {
    super(pagination, data);
  }

  public Seq<Account> getAccounts() {
    return getData();
  }

  public List<Account> getAccountsAsJava() {
    return getDataAsJava();
  }
}
