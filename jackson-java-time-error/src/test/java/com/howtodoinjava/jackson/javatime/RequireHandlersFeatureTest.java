package com.howtodoinjava.jackson.javatime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * What MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES does, and why disabling it is not a fix.
 */
class RequireHandlersFeatureTest {

  record DueDate(LocalDate dueOn) {}

  @Test
  void featureIsEnabledByDefault() {
    ObjectMapper mapper = new ObjectMapper();

    boolean enabled = mapper.isEnabled(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES);

    assertTrue(enabled);
  }

  @Test
  void disablingItSerializesLocalDateAsPlainBean() throws Exception {
    ObjectMapper mapper = JsonMapper.builder()
        .disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES)
        .build();

    String json = mapper.writeValueAsString(new DueDate(LocalDate.of(2026, 10, 1)));
    System.out.println("REQUIRE_HANDLERS_FOR_JAVA8_TIMES disabled: " + json);

    assertEquals("{\"dueOn\":{\"year\":2026,\"month\":\"OCTOBER\",\"monthValue\":10,\"dayOfMonth\":1,"
        + "\"leapYear\":false,\"dayOfWeek\":\"THURSDAY\",\"dayOfYear\":274,\"era\":\"CE\","
        + "\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\",\"isoBased\":true}}}", json);
  }

  @Test
  void disablingItStillCannotReadLocalDate() {
    ObjectMapper mapper = JsonMapper.builder()
        .disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES)
        .build();

    Exception ex = assertThrows(Exception.class,
        () -> mapper.readValue("{\"dueOn\":\"2026-10-01\"}", DueDate.class));
    System.out.println("read with feature disabled: " + ex.getClass().getSimpleName() + ": "
        + ex.getMessage().split("\n")[0]);

    assertEquals("com.fasterxml.jackson.databind.exc.InvalidDefinitionException", ex.getClass().getName());
    assertTrue(ex.getMessage().contains("InaccessibleObjectException"));
  }
}
