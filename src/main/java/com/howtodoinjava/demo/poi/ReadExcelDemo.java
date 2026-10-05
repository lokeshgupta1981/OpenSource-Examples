package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

/**
 * Reads howtodoinjava_demo.xlsx (written by WriteExcelDemo) cell by cell.
 * Run WriteExcelDemo first.
 */
public class ReadExcelDemo {

  public static void main(String[] args) throws IOException {
    File file = new File("howtodoinjava_demo.xlsx");

    System.out.println("== Reading by cell type");
    readByCellType(file);

    System.out.println("== Reading with DataFormatter");
    readWithDataFormatter(file);
  }

  static void readByCellType(File file) throws IOException {
    // WorkbookFactory opens both .xls and .xlsx; true = read-only
    try (Workbook workbook = WorkbookFactory.create(file, null, true)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        for (Cell cell : row) {
          String value = switch (cell.getCellType()) {
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                ? cell.getLocalDateTimeCellValue().toLocalDate().toString()
                : String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> "?";
          };
          System.out.print(value + "\t");
        }
        System.out.println();
      }
    }
  }

  static void readWithDataFormatter(File file) throws IOException {
    DataFormatter formatter = new DataFormatter();
    try (Workbook workbook = WorkbookFactory.create(file, null, true)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        for (Cell cell : row) {
          System.out.print(formatter.formatCellValue(cell) + "\t");
        }
        System.out.println();
      }
    }
  }
}
