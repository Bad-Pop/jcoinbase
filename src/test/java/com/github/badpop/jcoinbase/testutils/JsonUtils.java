package com.github.badpop.jcoinbase.testutils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

  public static String readResource(final String resourcePath) throws IOException {
    URL url = JsonUtils.class.getResource(resourcePath);
    return Files.readString(Paths.get(url.getPath()), StandardCharsets.UTF_8);
  }
}
