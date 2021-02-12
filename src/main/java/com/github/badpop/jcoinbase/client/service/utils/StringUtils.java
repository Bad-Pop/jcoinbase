package com.github.badpop.jcoinbase.client.service.utils;

public interface StringUtils {

  static boolean isBlank(final CharSequence cs) {
    final int strLen = cs == null ? 0 : cs.length();
    if (strLen == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
