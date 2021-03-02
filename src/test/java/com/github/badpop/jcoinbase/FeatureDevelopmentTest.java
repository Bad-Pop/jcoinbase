package com.github.badpop.jcoinbase;

import com.github.badpop.jcoinbase.client.JCoinbaseClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@Disabled("For development purpose only")
@DisplayName("Test class for development purpose only")
public class FeatureDevelopmentTest {

  @Test
  @DisplayName("Test case for development purpose only")
  void main() {

    var client =
        JCoinbaseClientFactory.build(
            "B4FyyXIxMbtAlAfe", "34ltm3h8KBzFC66YqWXhYfp4RVM80loQ", 3, false, false);

    var data = client.user().fetchCurrentUser();

    log.info("Data : {}", data);
  }
}
