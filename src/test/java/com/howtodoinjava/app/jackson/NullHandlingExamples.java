package com.howtodoinjava.app.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class NullHandlingExamples {
  @Test
  void testSerialization() throws JsonProcessingException {
    RecordWithNulls record = new RecordWithNulls();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    //1 - Mapper Level
    //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    String json = mapper.writeValueAsString(record);

    System.out.println(json);

/*    RecordWithNulls newRecord = mapper.readValue(json, RecordWithNulls.class);

    System.out.println(newRecord);*/
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
//2. Class level
//@JsonInclude(JsonInclude.Include.NON_NULL)
class RecordWithNulls {

  private long id;

  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = StringFilter.class)
  private String text;

  //3. Field level
  @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
  private LocalDateTime timestamp;
  private Boolean status;
}

class StringFilter {
  @Override
  public boolean equals(Object value) {
    //custom logic
    return true;
  }
}