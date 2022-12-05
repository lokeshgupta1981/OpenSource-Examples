package com.howtodoinjava.demo.fastexcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ReadExcel {

  public static void main(String[] args) throws IOException {

    try (InputStream is = new FileInputStream("c:/temp/fastexcel-demo.xlsx");
        ReadableWorkbook wb = new ReadableWorkbook(is)) {

      StopWatch watch = new StopWatch();
      watch.start();
      wb.getSheets().forEach(sheet ->
      {
        try (Stream<Row> rows = sheet.openStream()) {

          rows.skip(1).forEach(r -> {
            BigDecimal num = r.getCellAsNumber(0).orElse(null);
            String str = r.getCellAsString(1).orElse(null);

            System.out.println("Cell str value :: " + num);
            System.out.println("Cell str value :: " + str);
          });

        } catch (Exception e) {
          e.printStackTrace();
        }
        watch.stop();
        System.out.println("Processing time :: " + watch.getTime(TimeUnit.MILLISECONDS));
      });
    }
  }
}
