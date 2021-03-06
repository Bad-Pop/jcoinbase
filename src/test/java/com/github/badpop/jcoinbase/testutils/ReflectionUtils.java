package com.github.badpop.jcoinbase.testutils;

import lombok.val;

public class ReflectionUtils {

  public static void setFieldValueForObject(Object object, String fieldName, String value)
      throws NoSuchFieldException, IllegalAccessException {
    try {
      val declaredField = object.getClass().getDeclaredField(fieldName);
      declaredField.setAccessible(true);
      declaredField.set(object, value);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static Object getFieldForObject(Object object, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {
    val declaredField = object.getClass().getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    return declaredField.get(object);
  }
}
