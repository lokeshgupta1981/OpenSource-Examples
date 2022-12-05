package com.howtodoinjava.demo.fastexcel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

public class WriteExcel {

  public static void main(String[] args) throws IOException {
    try (OutputStream os = new FileOutputStream("c:/temp/fastexcel-demo.xlsx")) {
      Workbook wb = new Workbook(os, "DemoExcel", "1.0");
      Worksheet ws = wb.newWorksheet("Sheet 1");

      StopWatch watch = new StopWatch();
      watch.start();

      ws.value(0, 0, "Column 1");
      ws.value(0, 1, "Column 2");
      ws.value(0, 2, "Column 3");
      ws.value(0, 3, "Column 4");
      ws.value(0, 4, "Column 5");
      ws.value(0, 5, "Column 6");
      ws.value(0, 6, "Column 7");
      ws.value(0, 7, "Column 8");
      ws.value(0, 8, "Column 9");
      ws.value(0, 9, "Column 10");

      for (int i = 1; i < 1_000_000; i++) {

        String value = "data-" + i;
        ws.value(i, 0, i);
        ws.value(i, 1, value);
        ws.value(i, 2, value);
        ws.value(i, 3, value);
        ws.value(i, 4, value);
        ws.value(i, 5, value);
        ws.value(i, 6, value);
        ws.value(i, 7, value);
        ws.value(i, 8, value);
        ws.value(i, 9, value);
      }

      wb.finish();
      watch.stop();

      System.out.println("Processing time :: " + watch.getTime(TimeUnit.MILLISECONDS));
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
