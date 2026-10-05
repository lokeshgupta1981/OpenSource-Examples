package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Adds a formula cell to formulaDemo.xlsx and evaluates it when reading.
 */
public class ExcelFormulaDemo {

  public static void main(String[] args) throws IOException {
    Path file = Path.of("formulaDemo.xlsx");
    writeSheetWithFormula(file);
    readSheetWithFormula(file.toFile());
  }

  static void writeSheetWithFormula(Path file) throws IOException {
    try (Workbook workbook = new XSSFWorkbook();
         OutputStream out = Files.newOutputStream(file)) {

      Sheet sheet = workbook.createSheet("Calculate Simple Interest");

      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("Principal");
      header.createCell(1).setCellValue("Rate (%)");
      header.createCell(2).setCellValue("Years");
      header.createCell(3).setCellValue("Interest (P*R*T/100)");

      Row dataRow = sheet.createRow(1);
      dataRow.createCell(0).setCellValue(14500d);
      dataRow.createCell(1).setCellValue(9.25);
      dataRow.createCell(2).setCellValue(3d);
      dataRow.createCell(3).setCellFormula("A2*B2*C2/100");

      workbook.write(out);
    }
    System.out.println("Excel with formula cells written successfully");
  }

  static void readSheetWithFormula(File file) throws IOException {
    try (Workbook workbook = WorkbookFactory.create(file, null, true)) {
      Sheet sheet = workbook.getSheetAt(0);
      Cell interest = sheet.getRow(1).getCell(3);

      String formula = interest.getCellFormula();
      double cached = interest.getNumericCellValue();

      FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
      CellValue result = evaluator.evaluate(interest);

      System.out.println("formula = " + formula);
      System.out.println("cached value without evaluation = " + cached);
      System.out.println("evaluated type = " + result.getCellType() + ", value = " + result.getNumberValue());

      // Print the whole sheet; evaluate() also returns the value of non-formula cells
      for (Row row : sheet) {
        for (Cell cell : row) {
          CellValue value = evaluator.evaluate(cell);
          String text = switch (value.getCellType()) {
            case NUMERIC -> String.valueOf(value.getNumberValue());
            case STRING -> value.getStringValue();
            default -> "";
          };
          System.out.print(text + "\t\t");
        }
        System.out.println();
      }
    }
  }
}
