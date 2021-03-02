package com.github.badpop.jcoinbase.testutils;

public class ReflectionUtils {

  public static void setFieldValueForObject(Object object, String fieldName, String value)
      throws NoSuchFieldException, IllegalAccessException {
    try {
      var declaredField = object.getClass().getDeclaredField(fieldName);
      declaredField.setAccessible(true);
      declaredField.set(object, value);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static Object getFieldForObject(Object object, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {
    var declaredField = object.getClass().getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    return declaredField.get(object);
  }
}
