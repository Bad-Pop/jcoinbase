package com.github.badpop.jcoinbase;

import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import com.github.badpop.jcoinbase.model.request.UpdateCurrentUserRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Disabled("For development purpose only")
@DisplayName("Test class for development purpose only")
class DevelopmentTest {

  @Test
  void test() {

    val client =
        JCoinbaseClientFactory.build(
            "B4FyyXIxMbtAlAfe", "34ltm3h8KBzFC66YqWXhYfp4RVM80loQ", "", 3, false);

    val actual = client.user().updateCurrentUser(UpdateCurrentUserRequest.builder().build());

    log.info("Data : {}", actual);
    assertThat(actual).isNotNull();
  }
}
