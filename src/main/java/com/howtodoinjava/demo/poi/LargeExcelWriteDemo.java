package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes a large sheet either with SXSSF (streaming) or with XSSF (all rows in memory).
 *
 * Usage: LargeExcelWriteDemo [sxssf|xssf] [rows]
 * Try with a small heap, e.g. java -Xmx128m ..., to see the difference.
 */
public class LargeExcelWriteDemo {

  static final String[] FRUITS = {"apple", "banana", "cherry"};

  public static void main(String[] args) throws IOException {
    String mode = args.length > 0 ? args[0] : "sxssf";
    int rows = args.length > 1 ? Integer.parseInt(args[1]) : 1_000_000;
    Path file = Path.of("large-" + mode + ".xlsx");

    long start = System.nanoTime();
    if (mode.equals("xssf")) {
      try (Workbook workbook = new XSSFWorkbook()) {
        fill(workbook, rows);
        write(workbook, file);
      }
    } else {
      // Keep 100 rows in memory; older rows are flushed to a temp file.
      // close() deletes the temp files, so try-with-resources is enough.
      try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
        workbook.setCompressTempFiles(true);
        fill(workbook, rows);
        write(workbook, file);
      }
    }
    long millis = (System.nanoTime() - start) / 1_000_000;
    System.out.printf("%s: %,d rows written to %s (%,d KB) in %,d ms%n",
        mode, rows, file, Files.size(file) / 1024, millis);
  }

  static void fill(Workbook workbook, int rows) {
    Sheet sheet = workbook.createSheet("Stock");
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("Id");
    header.createCell(1).setCellValue("Fruit");
    header.createCell(2).setCellValue("Quantity");

    for (int i = 1; i <= rows; i++) {
      Row row = sheet.createRow(i);
      row.createCell(0).setCellValue(i);
      row.createCell(1).setCellValue(FRUITS[i % FRUITS.length]);
      row.createCell(2).setCellValue(i % 50);
    }
  }

  static void write(Workbook workbook, Path file) throws IOException {
    try (OutputStream out = Files.newOutputStream(file)) {
      workbook.write(out);
    }
  }
}
