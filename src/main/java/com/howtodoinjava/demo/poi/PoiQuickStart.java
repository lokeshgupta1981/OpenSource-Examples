package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes a one-row .xlsx file and reads it back. Shown in the intro of the article.
 */
public class PoiQuickStart {

  public static void main(String[] args) throws IOException {

    // 1. Write a workbook
    try (Workbook workbook = new XSSFWorkbook();
         OutputStream out = Files.newOutputStream(Path.of("fruits.xlsx"))) {
      Row row = workbook.createSheet("Stock").createRow(0);
      row.createCell(0).setCellValue("apple");
      row.createCell(1).setCellValue(5);
      workbook.write(out);
    }

    // 2. Read it back (null = no password, true = read-only)
    try (Workbook workbook = WorkbookFactory.create(new File("fruits.xlsx"), null, true)) {
      Row row = workbook.getSheetAt(0).getRow(0);
      DataFormatter formatter = new DataFormatter();
      String fruit = formatter.formatCellValue(row.getCell(0));      // "apple"
      String quantity = formatter.formatCellValue(row.getCell(1));   // "5"
      double raw = row.getCell(1).getNumericCellValue();             // 5.0
      System.out.println(fruit + " = " + quantity + " (raw " + raw + ")");
    }
  }
}
