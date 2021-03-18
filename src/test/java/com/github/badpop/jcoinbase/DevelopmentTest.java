package com.github.badpop.jcoinbase;

import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@Disabled("For development purpose only")
@DisplayName("Test class for development purpose only")
public class DevelopmentTest {

  @Test
  void test() {

    val client =
        JCoinbaseClientFactory.build(
            "B4FyyXIxMbtAlAfe", "34ltm3h8KBzFC66YqWXhYfp4RVM80loQ", "", 3, false);

    val actual = client.user().getAuthorizations().get().getScopesAsJava();

    log.info("Data : {}", actual);
  }
}
