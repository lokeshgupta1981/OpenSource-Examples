package com.howtodoinjava.llm;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TokenCountExample {

  public static void main(String[] args) {
    Encoding encoding = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.O200K_BASE);

    String sentence = "Spring Boot makes Java microservices easy to build.";
    int tokens = encoding.countTokens(sentence);
    System.out.println("Words: " + sentence.split("\\s+").length + ", tokens: " + tokens);

    IntArrayList ids = encoding.encode("unbelievably");
    List<String> pieces = IntStream.range(0, ids.size())
        .mapToObj(i -> {
          IntArrayList one = new IntArrayList();
          one.add(ids.get(i));
          return encoding.decode(one);
        })
        .toList();
    System.out.println("unbelievably -> " + ids.size() + " tokens: " + pieces);

    int codeTokens = encoding.countTokens("List<String> names = new ArrayList<>();");
    System.out.println("Code line tokens: " + codeTokens);
  }
}
