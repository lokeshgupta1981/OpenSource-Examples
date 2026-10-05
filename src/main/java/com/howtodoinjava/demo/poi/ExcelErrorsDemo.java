package com.howtodoinjava.demo.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reproduces the most common Apache POI errors and shows the fix for each one.
 */
public class ExcelErrorsDemo {

  public static void main(String[] args) throws Exception {
    Path xls = Path.of("old-format.xls");
    Path xlsx = Path.of("new-format.xlsx");
    Path csv = Path.of("report-really-csv.xlsx");
    Path bomb = Path.of("highly-compressed.xlsx");

    // Test files
    try (Workbook wb = new HSSFWorkbook(); OutputStream out = Files.newOutputStream(xls)) {
      wb.createSheet("Stock").createRow(0).createCell(0).setCellValue("apple");
      wb.write(out);
    }
    try (Workbook wb = new XSSFWorkbook(); OutputStream out = Files.newOutputStream(xlsx)) {
      Row row = wb.createSheet("Stock").createRow(0);
      row.createCell(0).setCellValue("apple");
      row.createCell(1).setCellValue(5);
      wb.write(out);
    }
    Files.writeString(csv, "fruit,quantity\napple,5\n");
    // Inline strings (no shared strings table): the sheet XML repeats the same long text
    try (SXSSFWorkbook wb = new SXSSFWorkbook(null, 100, true, false);
         OutputStream out = Files.newOutputStream(bomb)) {
      Sheet sheet = wb.createSheet("Notes");
      for (int i = 0; i < 100; i++) {
        sheet.createRow(i).createCell(0).setCellValue("a".repeat(30_000));
      }
      wb.write(out);
    }

    // 1. .xls file opened with XSSFWorkbook
    attempt("xls with XSSFWorkbook", () -> {
      try (InputStream in = Files.newInputStream(xls); Workbook wb = new XSSFWorkbook(in)) {
        return wb.getSheetAt(0).getSheetName();
      }
    });

    // 2. .xlsx file opened with HSSFWorkbook
    attempt("xlsx with HSSFWorkbook", () -> {
      try (InputStream in = Files.newInputStream(xlsx); Workbook wb = new HSSFWorkbook(in)) {
        return wb.getSheetAt(0).getSheetName();
      }
    });

    // 3. CSV file with an .xlsx extension
    attempt("csv with XSSFWorkbook", () -> {
      try (InputStream in = Files.newInputStream(csv); Workbook wb = new XSSFWorkbook(in)) {
        return wb.getSheetAt(0).getSheetName();
      }
    });
    attempt("csv with WorkbookFactory", () -> {
      try (Workbook wb = WorkbookFactory.create(csv.toFile())) {
        return wb.getSheetAt(0).getSheetName();
      }
    });

    // Fix for 1 and 2: WorkbookFactory detects the format from the file content
    for (Path path : new Path[]{xls, xlsx}) {
      attempt("WorkbookFactory " + path, () -> {
        try (Workbook wb = WorkbookFactory.create(path.toFile(), null, true)) {
          return wb.getClass().getSimpleName() + " " + wb.getSheetAt(0).getRow(0).getCell(0);
        }
      });
    }

    // 4. Wrong getter for the cell type
    attempt("getStringCellValue on a number", () -> {
      try (Workbook wb = WorkbookFactory.create(xlsx.toFile(), null, true)) {
        Cell quantity = wb.getSheetAt(0).getRow(0).getCell(1);
        return quantity.getStringCellValue();
      }
    });
    attempt("DataFormatter on a number", () -> {
      try (Workbook wb = WorkbookFactory.create(xlsx.toFile(), null, true)) {
        Cell quantity = wb.getSheetAt(0).getRow(0).getCell(1);
        return new DataFormatter().formatCellValue(quantity);
      }
    });

    // 5. Missing row or cell returns null
    attempt("missing row", () -> {
      try (Workbook wb = WorkbookFactory.create(xlsx.toFile(), null, true)) {
        return wb.getSheetAt(0).getRow(5).getCell(0).toString();
      }
    });
    attempt("missing row, safe", () -> {
      try (Workbook wb = WorkbookFactory.create(xlsx.toFile(), null, true)) {
        Row row = wb.getSheetAt(0).getRow(5);
        String safe = (row == null) ? "" : new DataFormatter().formatCellValue(row.getCell(0));
        return "\"" + safe + "\"";
      }
    });
    attempt("missing cell with policy", () -> {
      try (Workbook wb = WorkbookFactory.create(xlsx.toFile(), null, true)) {
        Row row = wb.getSheetAt(0).getRow(0);
        Cell cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return "type=" + cell.getCellType();
      }
    });

    // 6. Zip bomb check on a highly compressed file
    attempt("zip bomb check", () -> {
      try (Workbook wb = WorkbookFactory.create(bomb.toFile(), null, true)) {
        return "rows=" + wb.getSheetAt(0).getPhysicalNumberOfRows();
      }
    });
    attempt("zip bomb check, lower ratio", () -> {
      ZipSecureFile.setMinInflateRatio(0.001);    // only for files we trust
      try (Workbook wb = WorkbookFactory.create(bomb.toFile(), null, true)) {
        return "rows=" + wb.getSheetAt(0).getPhysicalNumberOfRows();
      } finally {
        ZipSecureFile.setMinInflateRatio(0.01);   // restore the default
      }
    });
  }

  interface Step {
    String run() throws Exception;
  }

  static void attempt(String name, Step step) {
    try {
      System.out.println("[" + name + "] OK: " + step.run());
    } catch (Exception e) {
      System.out.println("[" + name + "] " + e.getClass().getName() + ": " + e.getMessage());
    }
  }
}
