package com.github.badpop.jcoinbase.testutils;

import com.github.badpop.jcoinbase.client.JCoinbaseProperties;
import com.github.badpop.jcoinbase.client.JCoinbasePropertiesFactory;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

  public static JCoinbaseProperties invokeJcoinbasePropertiesFactoryBuildMethod(
      final String apiKey, final String secret, final String version, final boolean threadSafe)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    Method buildProtectedMethod =
        JCoinbasePropertiesFactory.class.getDeclaredMethod(
            "build", String.class, String.class, String.class, boolean.class);

    buildProtectedMethod.setAccessible(true);

    return (JCoinbaseProperties)
        buildProtectedMethod.invoke(new InternalProperties(), apiKey, secret, version, threadSafe);
  }

  public static class InternalProperties extends JCoinbasePropertiesFactory {}
}
