Source code for the article https://howtodoinjava.com/gson/gson/

# Gson Tutorial: Read and Write JSON

Small runnable classes for the Gson tutorial. Each class has its own `main()` and prints
the JSON or the parsed object it talks about in the article. The domain is a library
catalog with a *Book* class.

## Versions

- Gson 2.14.0
- JDK 25 (tested with Temurin 25.0.4.1), Maven 3.9.16

## Contents

- *Book* is the plain Java class every example reads and writes.
- *QuickStartExample* shows *toJson()* and *fromJson()*, missing fields, unknown fields and *JsonSyntaxException*.
- *CollectionsExample* reads and writes a *List*, an array and a *Map* with *TypeToken*.
- *GsonBuilderOptionsExample* shows pretty printing, *serializeNulls()*, *setDateFormat()* and *FieldNamingPolicy*.
- *AnnotationsExample* shows *@SerializedName* (with alternate names), *@Expose* and *transient* fields.
- *JsonTreeExample* parses JSON with *JsonParser* into *JsonElement*, *JsonObject* and *JsonArray*, changes the tree and writes it back.
- *LocalDateAdapter* and *TypeAdapterExample* register a custom *TypeAdapter* for *LocalDate*.
- *RecordExample* reads and writes a Java record.
- The older classes (*SerializationExample*, *DeserializationExample*, *SerializeDeserializeMap*, *SerializeDeserializeSet*, *SerializeNulls*, *DateFormatExample*, *JsonElementExample* and the *adapters* package) belong to the other Gson articles on the site.

## Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.howtodoinjava.demo.gson.QuickStartExample
```

Replace the class name with any class from the list above. The date examples print the
default `java.util.Date` format in the JVM's locale and time zone, so that one line can look
different on your machine.
