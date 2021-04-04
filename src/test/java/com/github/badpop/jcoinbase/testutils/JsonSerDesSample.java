package com.github.badpop.jcoinbase.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.jackson.datatype.VavrModule;

import java.time.ZoneId;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public abstract class JsonSerDesSample {

  public static final ObjectMapper JSON_SER_DES =
      new ObjectMapper()
          .findAndRegisterModules()
          .registerModule(new VavrModule())
          .registerModule(new JavaTimeModule())
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
          .configure(WRITE_DATES_AS_TIMESTAMPS, false);
}
