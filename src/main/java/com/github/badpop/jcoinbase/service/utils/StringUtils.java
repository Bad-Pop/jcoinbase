package com.github.badpop.jcoinbase.service.utils;

/** An utility interface that giving access to methods that validate Strings */
public interface StringUtils {

  /**
   * Checks if a CharSequence is empty (""), null or whitespace only. Whitespace is defined by
   * {@link Character#isWhitespace(char)}.
   *
   * @param cs the char sequence to verify
   * @return true if blank, false otherwise
   */
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
