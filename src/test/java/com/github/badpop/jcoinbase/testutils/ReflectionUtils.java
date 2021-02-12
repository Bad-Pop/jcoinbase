package com.github.badpop.jcoinbase.testutils;

public class ReflectionUtils {

  public static void setFieldValueForObject(Object object, String fieldName, String value) {
    try {
      var declaredField = object.getClass().getDeclaredField(fieldName);
      declaredField.setAccessible(true);
      declaredField.set(object, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
