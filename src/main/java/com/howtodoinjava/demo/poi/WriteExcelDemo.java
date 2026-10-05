package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes the "Employee Data" sheet to howtodoinjava_demo.xlsx in the working directory.
 */
public class WriteExcelDemo {

  public static void main(String[] args) throws IOException {
    Path file = Path.of("howtodoinjava_demo.xlsx");

    // Data to write, one Object[] per row
    List<Object[]> data = List.of(
        new Object[]{"ID", "NAME", "LASTNAME"},
        new Object[]{1, "Amit", "Shukla"},
        new Object[]{2, "Lokesh", "Gupta"},
        new Object[]{3, "John", "Adwards"},
        new Object[]{4, "Brian", "Schultz"});

    // Workbook and stream are closed by try-with-resources
    try (Workbook workbook = new XSSFWorkbook();
         OutputStream out = Files.newOutputStream(file)) {

      Sheet sheet = workbook.createSheet("Employee Data");

      int rowNum = 0;
      for (Object[] values : data) {
        Row row = sheet.createRow(rowNum++);
        int cellNum = 0;
        for (Object value : values) {
          Cell cell = row.createCell(cellNum++);
          switch (value) {
            case String text -> cell.setCellValue(text);
            case Integer number -> cell.setCellValue(number);
            default -> throw new IllegalArgumentException("Unsupported type: " + value);
          }
        }
      }
      workbook.write(out);
    }
    System.out.println(file + " written successfully on disk.");
  }
}
